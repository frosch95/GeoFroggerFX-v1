/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.frosch95.geofrogger.service;

import de.frosch95.geofrogger.application.ProgressEvent;
import de.frosch95.geofrogger.application.ProgressListener;
import de.frosch95.geofrogger.application.ServiceManager;
import de.frosch95.geofrogger.model.Cache;
import de.frosch95.geofrogger.model.User;
import de.frosch95.geofrogger.model.Waypoint;
import de.frosch95.geofrogger.sql.DatabaseService;

import java.net.MalformedURLException;
import java.net.URL;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Andreas
 */
public class CacheServiceImpl implements CacheService {

  private static final String SAVE_CACHE = "INSERT INTO geocache("
      + "id,"
      + "available,"
      + "archived,"
      + "name,"
      + "placedBy,"
      + "ownerId,"
      + "type,"
      + "container,"
      + "difficulty,"
      + "terrain,"
      + "country,"
      + "state,"
      + "shortDescription,"
      + "shortDescriptionHtml,"
      + "longDescription,"
      + "longDescriptionHtml,"
      + "encodedHints,"
      + "mainWaypointId"
      + ") values ("
      + "?,"
      + "?,"
      + "?,"
      + "?,"
      + "?,"
      + "?,"
      + "?,"
      + "?,"
      + "?,"
      + "?,"
      + "?,"
      + "?,"
      + "?,"
      + "?,"
      + "?,"
      + "?,"
      + "?,"
      + "?"
      + ")";

  private static final String SAVE_WAYPOINT = "INSERT INTO waypoint("
      + "id,"
      + "latitude,"
      + "longitude,"
      + "name,"
      + "time,"
      + "description,"
      + "url,"
      + "urlName,"
      + "symbol,"
      + "type"
      + ") values ("
      + "?,"
      + "?,"
      + "?,"
      + "?,"
      + "?,"
      + "?,"
      + "?,"
      + "?,"
      + "?,"
      + "?"
      + ")";

  private static final String COUNT_ALL_CACHES = "SELECT count(*) FROM geocache";
  private static final String LOAD_ALL_CACHES = "SELECT * FROM geocache ORDER BY ID";
  private static final String LOAD_CACHE_BY_ID = "SELECT * FROM geocache WHERE ID = ?";
  private static final String LOAD_ALL_WAYPOINTS = "SELECT * FROM waypoint ORDER BY ID";

  private final DatabaseService dbService = ServiceManager.getInstance().getDatabaseService();

  private final List<ProgressListener> listeners = new ArrayList<>();

  @Override
  public void addListener(ProgressListener listener) {
    if (!listeners.contains(listener)) {
      listeners.add(listener);
    }
  }

  @Override
  public void storeCaches(List<Cache> caches) {
    Connection connection = null;
    try {
      connection = dbService.getConnection();
      try (PreparedStatement cacheStatement = connection.prepareStatement(SAVE_CACHE)) {
        try (PreparedStatement waypointStatement = connection.prepareStatement(SAVE_WAYPOINT)) {
          int currentCacheNumber = 0;
          int numberOfCaches = caches.size();
          for (Cache cache : caches) {
            currentCacheNumber++;
            fireEvent(new ProgressEvent("Database",
                ProgressEvent.State.RUNNING,
                "Save caches to Database " + currentCacheNumber + " / " + numberOfCaches,
                (double) currentCacheNumber / (double) numberOfCaches));

            if (!doesCacheExist(cache.getId())) {
              saveCacheObject(cacheStatement, cache);
              saveWaypointObject(waypointStatement, cache);
            }
          }
        }
      }
      connection.commit();
    } catch (SQLException ex) {
      Logger.getLogger(CacheServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
      if (connection != null) {
        try {
          connection.rollback();
        } catch (SQLException ex1) {
          Logger.getLogger(CacheServiceImpl.class.getName()).log(Level.SEVERE, null, ex1);
        }
      }
    }
  }

  private boolean doesCacheExist(Long id) {
    boolean exists = false;
    try {
      try (PreparedStatement cacheStatement = dbService.getConnection().prepareStatement(LOAD_CACHE_BY_ID)) {
        cacheStatement.setLong(1, id);
        try (ResultSet cacheResultSet = cacheStatement.executeQuery()) {
          exists = cacheResultSet.next();
        }
      }
    } catch (SQLException ex) {
      Logger.getLogger(CacheServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
    }
    return exists;
  }

  @Override
  public List<Cache> getAllCaches() {
    List<Cache> caches = new ArrayList<>();
    Connection connection;
    try {
      connection = dbService.getConnection();
      try (PreparedStatement cacheStatement = connection.prepareStatement(LOAD_ALL_CACHES)) {
        caches = resultSetToCacheList(cacheStatement, connection);
      }
      connection.commit();
    } catch (SQLException ex) {
      Logger.getLogger(CacheServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
    }
    return caches;
  }

  private void saveCacheObject(PreparedStatement cacheStatement, Cache cache) throws SQLException {
    cacheStatement.setLong(1, cache.getId());
    cacheStatement.setBoolean(2, cache.isAvailable());
    cacheStatement.setBoolean(3, cache.isArchived());
    cacheStatement.setString(4, cache.getName());
    cacheStatement.setString(5, cache.getPlacedBy());
    cacheStatement.setLong(6, cache.getOwner().getId()); // ownerid
    cacheStatement.setString(7, cache.getType()); // type
    cacheStatement.setString(8, cache.getContainer()); //container
    cacheStatement.setString(9, cache.getDifficulty()); //difficulty
    cacheStatement.setString(10, cache.getTerrain()); //terrain
    cacheStatement.setString(11, cache.getCountry()); //country
    cacheStatement.setString(12, cache.getState()); //state
    cacheStatement.setString(13, cache.getShortDescription()); //shortDescription
    cacheStatement.setBoolean(14, cache.isShortDescriptionHtml()); //shortDescriptionHtml
    cacheStatement.setString(15, cache.getLongDescription()); //longDescription
    cacheStatement.setBoolean(16, cache.isLongDescriptionHtml()); //longDescriptionHtml
    cacheStatement.setString(17, cache.getEncodedHints()); //encodedHints
    cacheStatement.setLong(18, cache.getId()); //mainWaypointId
    cacheStatement.execute();
  }

  private void saveWaypointObject(PreparedStatement waypointStatement, Cache cache) throws SQLException {
    waypointStatement.setLong(1, cache.getId());
    waypointStatement.setDouble(2, cache.getMainWayPoint().getLatitude()); // latitude
    waypointStatement.setDouble(3, cache.getMainWayPoint().getLongitude()); //longitude,"
    waypointStatement.setString(4, cache.getMainWayPoint().getName()); //name,"
    waypointStatement.setTimestamp(5, Timestamp.valueOf(cache.getMainWayPoint().getTime())); //time,"
    waypointStatement.setString(6, cache.getMainWayPoint().getDescription()); //description,"
    waypointStatement.setString(7, cache.getMainWayPoint().getUrl().toExternalForm()); //url,"
    waypointStatement.setString(8, cache.getMainWayPoint().getUrlName()); //urlName,"
    waypointStatement.setString(9, cache.getMainWayPoint().getSymbol()); //symbol,"
    waypointStatement.setString(10, cache.getMainWayPoint().getType()); //type"
    waypointStatement.execute();
  }

  private List<Cache> resultSetToCacheList(final PreparedStatement cacheStatement, Connection connection) throws SQLException {

    final List<Cache> caches = new ArrayList<>();

    int numberOfCaches = 0;
    try (PreparedStatement countStatement = connection.prepareStatement(COUNT_ALL_CACHES)) {
      try (ResultSet countResultSet = countStatement.executeQuery()) {
        if (countResultSet.next()) {
          numberOfCaches = countResultSet.getInt(1);
        }
      }
    }

    try (ResultSet cacheResultSet = cacheStatement.executeQuery()) {
      int currentCacheNumber = 0;
      while (cacheResultSet.next()) {
        currentCacheNumber++;
        fireEvent(new ProgressEvent("Database",
            ProgressEvent.State.RUNNING,
            "Load caches from Database " + currentCacheNumber + " / " + numberOfCaches,
            (double) currentCacheNumber / (double) numberOfCaches));
        createCacheObject(cacheResultSet, caches);
      }
    }

    final List<Waypoint> waypoints = new ArrayList<>();
    try (PreparedStatement waypointStatement = connection.prepareStatement(LOAD_ALL_WAYPOINTS)) {
      try (ResultSet waypointResultSet = waypointStatement.executeQuery()) {
        int currentWaypointNumber = 0;
        while (waypointResultSet.next()) {
          currentWaypointNumber++;
          fireEvent(new ProgressEvent("Database",
              ProgressEvent.State.RUNNING,
              "Load waypoints from Database " + currentWaypointNumber + " / " + numberOfCaches,
              (double) currentWaypointNumber / (double) numberOfCaches));
          createWaypointObject(waypointResultSet, waypoints);
        }
      }
    }

    int cacheSize = caches.size();
    int waypointSize = waypoints.size();

    assert cacheSize == waypointSize : "size of waypoints and size of caches have to be the same!!";
    for (int i = 0; i < cacheSize; i++) {
      final Cache cache = caches.get(i);
      final Waypoint waypoint = waypoints.get(i);

      assert cache.getId().equals(waypoint.getId());

      fireEvent(new ProgressEvent("Database",
          ProgressEvent.State.RUNNING,
          "Add waypoints to caches " + i + " / " + cacheSize,
          (double) i / (double) cacheSize));

      cache.setMainWayPoint(waypoint);

    }
    return caches;
  }

  private void fireEvent(ProgressEvent event) {
    listeners.stream().forEach((l) -> {
      l.progress(event);
    });
  }

  private void createCacheObject(final ResultSet cacheResultSet, final List<Cache> caches) throws SQLException {
    Cache cache = new Cache();
    cache.setId(cacheResultSet.getLong("id"));
    cache.setAvailable(cacheResultSet.getBoolean("available"));
    cache.setAvailable(cacheResultSet.getBoolean("archived"));
    cache.setName(cacheResultSet.getString("name"));
    cache.setPlacedBy(cacheResultSet.getString("placedBy"));
    cache.setType(cacheResultSet.getString("type"));
    cache.setContainer(cacheResultSet.getString("container"));
    cache.setDifficulty(cacheResultSet.getString("difficulty"));
    cache.setTerrain(cacheResultSet.getString("terrain"));
    cache.setCountry(cacheResultSet.getString("country"));
    cache.setState(cacheResultSet.getString("state"));
    cache.setShortDescription(cacheResultSet.getString("shortDescription"));
    cache.setShortDescriptionHtml(cacheResultSet.getBoolean("shortDescriptionHtml"));
    cache.setLongDescription(cacheResultSet.getString("longDescription"));
    cache.setLongDescriptionHtml(cacheResultSet.getBoolean("longDescriptionHtml"));
    cache.setEncodedHints(cacheResultSet.getString("encodedHints"));

    User user = new User();
    user.setName("aus der DB");
    user.setId(cacheResultSet.getLong("ownerId"));
    cache.setOwner(user);
    caches.add(cache);
  }

  private void createWaypointObject(final ResultSet waypointResultSet, final List<Waypoint> waypoints) throws SQLException {
    Waypoint waypoint = new Waypoint();
    //cache.setMainWayPoint(waypoint);
    waypoint.setId(waypointResultSet.getLong("id"));
    waypoint.setLatitude(waypointResultSet.getDouble("latitude"));
    waypoint.setLongitude(waypointResultSet.getDouble("longitude"));
    waypoint.setName(waypointResultSet.getString("name"));
    waypoint.setTime(waypointResultSet.getTimestamp("time").toLocalDateTime());
    waypoint.setDescription(waypointResultSet.getString("description"));
    try {
      waypoint.setUrl(new URL(waypointResultSet.getString("url")));
    } catch (MalformedURLException ex) {
      Logger.getLogger(CacheServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
    }
    waypoint.setUrlName(waypointResultSet.getString("urlName"));
    waypoint.setSymbol(waypointResultSet.getString("symbol"));
    waypoint.setType(waypointResultSet.getString("type"));
    waypoints.add(waypoint);
  }

}
