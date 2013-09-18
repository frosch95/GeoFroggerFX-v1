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
package de.frosch95.geofrogger.fx.components;

import de.frosch95.geofrogger.model.Cache;
import javafx.scene.image.Image;

/**
 * @author Andreas
 */
public class GeocachingIcons {

  public static Image getIcon(Cache cache) {
    return getIcon(cache, IconManager.IconSize.MIDDLE);
  }

  public static Image getIcon(Cache cache, IconManager.IconSize size) {
    String iconName = "/icons/iconmonstr-map-5-icon.png";

    switch (cache.getType()) {
      case "Multi-cache":
        iconName = "/icons/iconmonstr-map-6-icon.png";
        break;

      case "Traditional Cache":
        iconName = "/icons/iconmonstr-map-5-icon.png";
        break;

      case "Unknown Cache":
        iconName = "/icons/iconmonstr-help-3-icon.png";
        break;

      case "Earthcache":
        iconName = "/icons/iconmonstr-globe-4-icon.png";
        break;

      case "Letterbox Hybrid":
        iconName = "/icons/iconmonstr-email-4-icon.png";
        break;

      case "Event Cache":
        iconName = "/icons/iconmonstr-calendar-4-icon.png";
        break;

      case "Wherigo Cache":
        iconName = "/icons/iconmonstr-navigation-6-icon.png";
        break;

      case "Webcam Cache":
        iconName = "/icons/iconmonstr-webcam-3-icon.png";
        break;

      case "Virtual Cache":
        iconName = "/icons/iconmonstr-network-2-icon.png";
        break;

      default:
        System.out.println(cache.getType());
    }


    return IconManager.getIcon(iconName, size);
  }

  public static Image getMapIcon(Cache cache) {
    return getMapIcon(cache, IconManager.IconSize.MIDDLE);
  }

  public static Image getMapIcon(Cache cache, IconManager.IconSize size) {
    String iconName = "/icons/iconmonstr-location-icon.png";
    return IconManager.getIcon(iconName, size);
  }

}
