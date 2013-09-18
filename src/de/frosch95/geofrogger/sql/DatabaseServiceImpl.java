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
package de.frosch95.geofrogger.sql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Andreas
 */
public class DatabaseServiceImpl implements DatabaseService {

  private static final String CREATE_GEOCACHE_TABLE =
      "CREATE TABLE geocache ("
          + "id BIGINT,"
          + "available BOOLEAN,"
          + "archived BOOLEAN,"
          + "name VARCHAR(255),"
          + "placedBy VARCHAR(255),"
          + "ownerId BIGINT,"
          + "type VARCHAR(255),"
          + "container VARCHAR(255),"
          + "difficulty VARCHAR(10),"
          + "terrain VARCHAR(10),"
          + "country VARCHAR(255),"
          + "state VARCHAR(255),"
          + "shortDescription CLOB,"
          + "shortDescriptionHtml BOOLEAN,"
          + "longDescription CLOB,"
          + "longDescriptionHtml BOOLEAN,"
          + "encodedHints CLOB,"
          + "mainWaypointId BIGINT"
          + ");";

  private static final String WAYPOINT_TABLE =
      "CREATE TABLE waypoint ("
          + "id BIGINT,"
          + "latitude DECIMAL(9,6),"
          + "longitude DECIMAL(9,6),"
          + "name VARCHAR(255),"
          + "time TIMESTAMP,"
          + "description CLOB,"
          + "url VARCHAR(255),"
          + "urlName VARCHAR(255),"
          + "symbol VARCHAR(255),"
          + "type VARCHAR(255)"
          + ");";


  private Connection con;

  public DatabaseServiceImpl() {
    try {
      con = DriverManager.getConnection("jdbc:h2:./geofroggerfxdb;IFEXISTS=TRUE", "sa", "sa");
      con.setAutoCommit(false);
    } catch (SQLException ex) {
      setupDatabase();
    }
  }

  @Override
  public Connection getConnection() throws SQLException {
    if (con == null) {
      throw new SQLException("no connection available");
    }
    return con;
  }

  private void setupDatabase() {
    Statement statement = null;
    try {
      Class.forName("org.h2.Driver");
      con = DriverManager.getConnection("jdbc:h2:./geofroggerfxdb", "sa", "sa");
      con.setAutoCommit(false);
      statement = con.createStatement();
      statement.execute(CREATE_GEOCACHE_TABLE);
      statement.execute(WAYPOINT_TABLE);
      con.commit();
    } catch (SQLException | ClassNotFoundException ex) {
      Logger.getLogger(DatabaseServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
    } finally {
      if (statement != null) try {
        statement.close();
      } catch (SQLException ex) {
        Logger.getLogger(DatabaseServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
      }
    }
  }


}
