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
package de.geofroggerfx.application;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Andreas
 */
public class SessionContext {

  private static final SessionContext INSTANCE = new SessionContext();
  private final Map<String, Object> map = new ConcurrentHashMap<>();
  private final Map<String, List<SessionContextListener>> sessionListeners = new HashMap<>();

  private SessionContext() {
  }

  public static SessionContext getInstance() {
    return INSTANCE;
  }

  public void setData(String key, Object value) {
    if (value == null && map.containsKey(key)) {
      map.remove(key);
    } else if (value != null) {
      map.put(key, value);
    }
    fireSessionEvent(key);
  }

  public Object getData(String key) {
    if (map.containsKey(key)) {
      return map.get(key);
    } else {
      return null;
    }
  }

  public void addListener(String key, SessionContextListener sessionListener) {

    List<SessionContextListener> listenerList;
    if (sessionListeners.containsKey(key)) {
      listenerList = sessionListeners.get(key);
    } else {
      listenerList = new ArrayList<>();
      sessionListeners.put(key, listenerList);
    }

    if (!listenerList.contains(sessionListener)) {
      listenerList.add(sessionListener);
    }
  }

  private void fireSessionEvent(String key) {
    if (sessionListeners.containsKey(key)) {
      final List<SessionContextListener> listenerList = sessionListeners.get(key);
      for (final SessionContextListener listener : listenerList) {
        listener.sessionContextChanged();
      }
    }
  }
}
