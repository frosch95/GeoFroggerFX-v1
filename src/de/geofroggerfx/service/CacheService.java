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

import de.geofroggerfx.application.ProgressListener;
import de.geofroggerfx.model.Cache;
import de.geofroggerfx.model.CacheList;

import javax.inject.Singleton;
import java.util.List;

/**
 * @author Andreas
 */
public interface CacheService {

  /**
   * Store the whole list of caches
   * @param caches list of caches
   */
  void storeCaches(List<Cache> caches);

  /**
   * find a single cache by id
   * @param id id of the cache
   * @return single cache
   */
  Cache findCacheById(Long id);

  /**
   * Receive all caches from the database
   * @param sortField sort the list based on the field
   * @param direction sort direction (ASC/DESC)
   * @return sorted list of caches
   */
  List<Cache> getAllCaches(CacheSortField sortField, SortDirection direction);

  /**
   * Add a ProgressListener to inform about loading/storing progress.
   * @param listener progress listener
   */
  void addListener(ProgressListener listener);

  /**
   * Receive all cache lists
   * @return list of available cachelists
   */
  List<CacheList> getAllCacheLists();

  /**
   * Test if a cache list with the given name already exists
   * @param name of the cache list
   * @return true if a list with the given name exists
   */
  boolean doesCacheListNameExist(String name);

  /**
   * Stores a cache list
   * @param list list of caches
   */
  void storeCacheList(CacheList list);

  /**
   * Deletes a cache list
   * @param cacheList deletes the given cache list
   */
  void deleteCacheList(CacheList cacheList);
}
