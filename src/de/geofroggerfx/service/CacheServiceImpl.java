/*
 * Copyright (c) 2013, Andreas Billmann <abi@geofroggerfx.de>
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * * Redistributions of source code must retain the above copyright notice, this
 *   list of conditions and the following disclaimer.
 * * Redistributions in binary form must reproduce the above copyright notice,
 *   this list of conditions and the following disclaimer in the documentation
 *   and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
package de.geofroggerfx.service;

import com.orientechnologies.orient.core.intent.OIntentMassiveInsert;
import com.orientechnologies.orient.core.sql.query.OSQLSynchQuery;
import com.orientechnologies.orient.object.db.OObjectDatabaseTx;
import de.geofroggerfx.application.ProgressEvent;
import de.geofroggerfx.application.ProgressListener;
import de.geofroggerfx.model.*;
import de.geofroggerfx.sql.DatabaseService;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Andreas
 */
public class CacheServiceImpl implements CacheService {

  @Inject
  private DatabaseService dbService;
  private final List<ProgressListener> listeners = new ArrayList<>();

  @Override
  public void addListener(ProgressListener listener) {
    if (!listeners.contains(listener)) {
      listeners.add(listener);
    }
  }

  @Override
  public void storeCaches(List<Cache> caches) {
    OObjectDatabaseTx database = dbService.getDatabase();

    try {
      long start = System.currentTimeMillis();
      for (Cache cache : caches) {
        Cache existingCache = findCacheById(cache.getId());
        if (existingCache != null) {
          database.delete(existingCache);
        }
      }

      long deletes = System.currentTimeMillis();

      int currentCacheNumber = 0;
      int numberOfCaches = caches.size();
      database.declareIntent( new OIntentMassiveInsert() );
      for (Cache cache : caches) {
        currentCacheNumber++;
        fireEvent(new ProgressEvent("Database",
            ProgressEvent.State.RUNNING,
            "Save caches to Database " + currentCacheNumber + " / " + numberOfCaches,
            (double) currentCacheNumber / (double) numberOfCaches));
        database.save(cache);
      }


      long saves = System.currentTimeMillis();

      System.out.println("Time to delete: "+(deletes-start));
      System.out.println("Time to save: "+(saves-deletes));
      System.out.println("Time total: "+(saves-start));

    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      database.declareIntent( null );
    }

    System.out.println("cache count: " + database.countClass(Cache.class));
    System.out.println("Log count: "+ database.countClass(Log.class));
    System.out.println("Waypoint count: "+ database.countClass(Waypoint.class));

    fireEvent(new ProgressEvent("Database",
          ProgressEvent.State.FINISHED,
          "Caches are saved to Database"));
  }

  @Override
  public Cache findCacheById(Long id) {
    Cache foundCache = null;

    try {
      OObjectDatabaseTx database = dbService.getDatabase();
      String query = "select * from Cache where id="+id;
      List<Cache> result = database.query(new OSQLSynchQuery<Cache>(query));
      if (result != null && result.size() > 0) {
        foundCache = result.get(0);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return foundCache;
  }


  @Override
  public List<Cache> getAllCaches(CacheSortField sortField, SortDirection direction) {

    List<Cache> caches = new ArrayList<>();
    try {
      OObjectDatabaseTx database = dbService.getDatabase();
      String query = "select * from Cache order by "+sortField.getFieldName()+" "+direction.toString();
      List<Cache> result = database.query(new OSQLSynchQuery<Cache>(query));
      if (result != null) {
        caches = result;
      }
    } catch (Exception e) {
      e.printStackTrace();
    }

    return caches;
  }

  private void fireEvent(ProgressEvent event) {
    listeners.stream().forEach((l) -> l.progress(event));
  }

  /**
   * Receive all cache lists
   */
  @Override
  public List<CacheList> getAllCacheLists() {
    List<CacheList> lists = new ArrayList<>();
    try {
      OObjectDatabaseTx database = dbService.getDatabase();
      String query = "select * from CacheList order by name";
      List<CacheList> result = database.query(new OSQLSynchQuery<CacheList>(query));
      if (result != null) {
        lists = result;
      }
    } catch (Exception e) {
      e.printStackTrace();
    }

    return lists;
  }

  @Override
  public boolean doesCacheListNameExist(String name) {
    boolean doesExist = false;
    try {
      OObjectDatabaseTx database = dbService.getDatabase();
      String query = "select * from CacheList l where l.name = :name";
      List<CacheList> result = database.query(new OSQLSynchQuery<CacheList>(query));
      if (result != null) {
        doesExist = result.size() > 0;
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return doesExist;
  }

  @Override
  public void deleteCacheList(CacheList cacheList) {
    OObjectDatabaseTx database = dbService.getDatabase();
    try {
      database.delete(cacheList);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Override
  public void storeCacheList(CacheList list) {
    OObjectDatabaseTx database = dbService.getDatabase();
    try {
      database.save(list);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
