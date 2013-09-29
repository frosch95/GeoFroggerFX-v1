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
package de.frosch95.geofrogger.model;

import java.util.List;

/**
 * @author Andreas Billmann <abi@geofroggerfx.de>
 */
public class CacheUtils {

  public static final String FOUND_IT = "Found it";
  public static final String ATTENDED = "Attended";
  public static final String WEBCAM_PHOTO = "Webcam Photo Taken";

  public static boolean hasUserFoundCache(Cache cache, Long currentUser) {
    boolean foundIt = false;
    if (currentUser != null) {
      List<Log> logs = cache.getLogs();
      if (logs != null) {
        for (Log log : logs) {
          if (isCurrentUserLogUser(log, currentUser) && isLogTypeFoundIt(log)) {
            foundIt = true;
            break;
          }
        }
      }
    }
    return foundIt;
  }

  private static boolean isCurrentUserLogUser(Log log, Long currentUser) {
    return log.getFinder().getId().equals(currentUser);
  }

  private static boolean isLogTypeFoundIt(Log log) {
    return log.getType().equals(FOUND_IT) || log.getType().equals(ATTENDED) || log.getType().equals(WEBCAM_PHOTO);
  }

}
