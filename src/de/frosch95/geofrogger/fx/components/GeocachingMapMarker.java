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
import javafx.collections.ObservableList;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import jfxtras.labs.map.MapControlable;
import jfxtras.labs.map.render.MapMarkable;

import java.awt.*;
import java.util.Collections;
import java.util.List;


/**
 * @author Andreas
 */
public class GeocachingMapMarker implements MapMarkable {

  private double lat;
  private double lon;
  private Label iconLabel;
  private Cache cache;

  public GeocachingMapMarker(Cache cache, double lat, double lon) {
    this.lat = lat;
    this.lon = lon;
    this.cache = cache;

    this.iconLabel = new Label();
    iconLabel.setFont(Font.font("FontAwesome", FontWeight.BOLD, 24));
    iconLabel.setText(AwesomeGeocachingIcons.getIcon(cache).toString());
  }

  @Override
  public double getLat() {
    return lat;
  }

  @Override
  public double getLon() {
    return lon;
  }

  @Override
  public void render(MapControlable mapController) {
    Point postion = mapController.getMapPoint(lat, lon, true);
    if (postion != null) {
      Group tilesGroup = mapController.getTilesGroup();
      ObservableList<Node> children = tilesGroup.getChildren();
      List<? extends Node> nodes = createChildren(postion);
      for (Node node : nodes) {
        if (!children.contains(node)) {
          children.add(node);
        }
      }
    }
  }

  List<? extends Node> createChildren(Point position) {
    iconLabel.setTranslateX(position.x);
    iconLabel.setTranslateY(position.y);
    return Collections.singletonList(iconLabel);
  }

  @Override
  public Node getNode() {
    return iconLabel;
  }
}

    

