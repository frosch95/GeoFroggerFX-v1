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
package de.frosch95.geofrogger.application;

import de.frosch95.geofrogger.gpx.GPXReader;
import de.frosch95.geofrogger.gpx.GroundspeakGPXReader;
import de.frosch95.geofrogger.service.CacheService;
import de.frosch95.geofrogger.service.CacheServiceImpl;
import de.frosch95.geofrogger.sql.DatabaseService;
import de.frosch95.geofrogger.sql.DatabaseServiceImpl;

/**
 * @author Andreas
 */
public class ServiceManager {

  private static final ServiceManager INSTANCE = new ServiceManager();

  private GPXReader gpxReader;
  private DatabaseService databaseService;
  private CacheService cacheService;

  private ServiceManager() {
    // private for singleton pattern
  }

  public static ServiceManager getInstance() {
    return INSTANCE;
  }

  public synchronized GPXReader getGPXReader() {
    if (gpxReader == null) {
      gpxReader = new GroundspeakGPXReader();
    }
    return gpxReader;
  }

  public synchronized DatabaseService getDatabaseService() {
    if (databaseService == null) {
      databaseService = new DatabaseServiceImpl();
    }
    return databaseService;
  }

  public synchronized CacheService getCacheService() {
    if (cacheService == null) {
      cacheService = new CacheServiceImpl();
    }
    return cacheService;
  }

}
