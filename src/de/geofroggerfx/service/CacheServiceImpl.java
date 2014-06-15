/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.geofroggerfx.service;

import de.geofroggerfx.application.ProgressEvent;
import de.geofroggerfx.application.ProgressListener;
import de.geofroggerfx.model.*;
import de.geofroggerfx.sql.DatabaseService;

import javax.inject.Inject;
import javax.persistence.EntityManager;
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
      EntityManager em = dbService.getEntityManager();

    try {
      int transactionNumber = 0;
      int currentCacheNumber = 0;
      int numberOfCaches = caches.size();
      for (Cache cache : caches) {
            currentCacheNumber++;
          fireEvent(new ProgressEvent("Database",
              ProgressEvent.State.RUNNING,
              "Save caches to Database " + currentCacheNumber + " / " + numberOfCaches,
              (double) currentCacheNumber / (double) numberOfCaches));

        // begin transaction if the transaction counter is set to zero
        if (transactionNumber == 0) { em.getTransaction().begin(); }
        transactionNumber++;

        em.merge(cache.getOwner());
        em.merge(cache.getMainWayPoint());

        for (Log log: cache.getLogs()) {
          em.merge(log);
          em.merge(log.getFinder());
        }

        for (Attribute attribute: cache.getAttributes()) {
          em.merge(attribute);
        }

        for (TravelBug bug: cache.getTravelBugs()) {
          em.merge(bug);
        }

        em.merge(cache);

        // comit every X caches
        if (transactionNumber == TRANSACTION_SIZE) {
          em.getTransaction().commit();
          transactionNumber = 0;
        }
      }

      // if there wasn?t a commit right before, commit the rest
      if (transactionNumber != 0) {
        em.getTransaction().commit();
      }
    } catch (Exception e) {
      e.printStackTrace();
      em.getTransaction().rollback();
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
      EntityManager em = dbService.getEntityManager();
      String query = "select c from Cache c order by c."+sortField.getFieldName()+" "+direction.toString();
      List<Cache> result = em.createQuery(query).getResultList();
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
      EntityManager em = dbService.getEntityManager();
      String query = "select l from CacheList l order by l.name";
      List<CacheList> result = em.createQuery(query).getResultList();
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
      EntityManager em = dbService.getEntityManager();
      String query = "select count(l) from CacheList l where l.name = :name";
      Long result = (Long)em.createQuery(query).setParameter("name", name).getSingleResult();
      doesExist = result > 0;
    } catch (Exception e) {
      e.printStackTrace();
    }
    return doesExist;
  }

  @Override
  public void deleteCacheList(CacheList cacheList) {
    EntityManager em = dbService.getEntityManager();
    try {
      em.getTransaction().begin();
      em.remove(cacheList);
      em.getTransaction().commit();
    } catch (Exception e) {
      e.printStackTrace();
      em.getTransaction().rollback();
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void storeCacheList(CacheList list) {
    EntityManager em = dbService.getEntityManager();
    try {
      em.getTransaction().begin();
      em.merge(list);
      em.getTransaction().commit();
    } catch (Exception e) {
      e.printStackTrace();
      em.getTransaction().rollback();
    }
  }
}
