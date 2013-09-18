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

/**
 * @author Andreas
 */
public class AwesomeGeocachingIcons {

  public static AwesomeIcons getIcon(Cache cache) {

    AwesomeIcons icon = AwesomeIcons.ICON_TAG;

    switch (cache.getType()) {
      case "Multi-cache":
        icon = AwesomeIcons.ICON_TAGS;
        break;

      case "Traditional Cache":
        icon = AwesomeIcons.ICON_TAG;
        break;

      case "Unknown Cache":
        icon = AwesomeIcons.ICON_QUESTION_SIGN;
        break;

      case "Earthcache":
        icon = AwesomeIcons.ICON_GLOBE;
        break;

      case "Letterbox Hybrid":
        icon = AwesomeIcons.ICON_INBOX;
        break;

      case "Event Cache":
        icon = AwesomeIcons.ICON_CALENDAR;
        break;

      case "Whereigo Cache":
        icon = AwesomeIcons.ICON_PLAY_SIGN;
        break;

      case "Webcam Cache":
        icon = AwesomeIcons.ICON_CAMERA;
        break;

      case "Virtual Cache":
        icon = AwesomeIcons.ICON_LAPTOP;
        break;


      default:
        System.out.println(cache.getType());
    }

    return icon;
  }

}
