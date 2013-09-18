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
package de.frosch95.geofrogger.fx;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.layout.Pane;
import jfxtras.labs.map.MapPane;
import jfxtras.labs.map.render.MapMarkable;
import jfxtras.labs.map.tile.OsmTileSourceFactory;

import java.util.List;

/**
 * @author Andreas Billmann <abi@geofroggerfx.de>
 */
public class MapPaneWrapper extends Pane {
  public static final String MAP_PANE_WRAPPER_CLASS = "map-pane-wrapper";

  private final MapPane mapPane;

  public MapPaneWrapper() {
    mapPane = new MapPane(new OsmTileSourceFactory().create());

    JavaFXUtils.addClasses(this, MAP_PANE_WRAPPER_CLASS);

    setSizeListener();
    this.getChildren().add(mapPane);
    setDisplayPositionByLatLon(32.81729, -117.215905, 9);
    mapPane.setMapMarkerVisible(true);
  }

  public final void setDisplayPositionByLatLon(double lat, double lon, int zoom) {
    mapPane.setDisplayPositionByLatLon(lat, lon, zoom);
  }

  public final void setMapMarkerList(List<MapMarkable> mapMarkerList) {
    mapPane.setMapMarkerList(mapMarkerList);
  }

  private void setSizeListener() {
    this.widthProperty().addListener(new ChangeListener() {
      @Override
      public void changed(ObservableValue observable, Object oldValue, Object newValue) {
        Double width = (Double) newValue;
        mapPane.setMapWidth(width);
      }
    });

    this.heightProperty().addListener(new ChangeListener() {
      @Override
      public void changed(ObservableValue observable, Object oldValue, Object newValue) {
        Double height = (Double) newValue;
        mapPane.setMapHeight(height);
      }
    });
  }
}
