/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.geofroggerfx.service;

import com.orientechnologies.orient.core.sql.query.OSQLSynchQuery;
import com.orientechnologies.orient.object.db.OObjectDatabaseTx;
import de.geofroggerfx.application.ProgressEvent;
import de.geofroggerfx.application.ProgressListener;
import de.geofroggerfx.model.Cache;
import de.geofroggerfx.model.CacheList;
import de.geofroggerfx.sql.DatabaseService;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Andreas
 */
public class CacheServiceImpl implements CacheService {

  private static final int TRANSACTION_SIZE = 100;

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
      int currentCacheNumber = 0;
      int numberOfCaches = caches.size();
      for (Cache cache : caches) {
            currentCacheNumber++;
          fireEvent(new ProgressEvent("Database",
              ProgressEvent.State.RUNNING,
              "Save caches to Database " + currentCacheNumber + " / " + numberOfCaches,
              (double) currentCacheNumber / (double) numberOfCaches));

        database.save(cache);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }

    fireEvent(new ProgressEvent("Database",
          ProgressEvent.State.FINISHED,
          "Caches are saved to Database"));
  }

  @Override
  @SuppressWarnings("unchecked")
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

  /**
   * {@inheritDoc}
   */
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
