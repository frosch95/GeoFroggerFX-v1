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
package de.geofroggerfx.database;

import com.orientechnologies.orient.core.db.ODatabaseRecordThreadLocal;
import com.orientechnologies.orient.object.db.OObjectDatabaseTx;

/**
 * @author Andreas
 */
public class DatabaseServiceImpl implements DatabaseService {

  private static final String PERSISTENCE_UNIT_NAME = "geocaches";
  private OObjectDatabaseTx db ;

  public DatabaseServiceImpl() {
    db = new OObjectDatabaseTx("plocal:./"+PERSISTENCE_UNIT_NAME);
    if (!db.exists()) {
      db.create();
    } else {
      db.open("admin", "admin");
    }


    db.getEntityManager().registerEntityClasses("de.geofroggerfx.model");
  }


  @Override
  public OObjectDatabaseTx getDatabase() {
    assert (db != null) : "no database available";
    ODatabaseRecordThreadLocal.INSTANCE.set(db.getUnderlying().getUnderlying());
    return db;
  }

  @Override
  public void close() {
    assert (db != null) : "no database available";
    if (!db.isClosed()) {
      db.close();
    }
  }

}
