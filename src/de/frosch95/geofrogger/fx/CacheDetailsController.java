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

import de.frosch95.geofrogger.application.SessionContext;
import de.frosch95.geofrogger.application.SessionContextListener;
import de.frosch95.geofrogger.fx.components.GeocachingIcons;
import de.frosch95.geofrogger.fx.components.IconManager;
import de.frosch95.geofrogger.model.Cache;
import javafx.animation.FadeTransition;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.util.Duration;
import jfxtras.labs.map.render.ImageMapMarker;
import jfxtras.labs.map.render.MapMarkable;

import java.net.URL;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.ResourceBundle;

/**
 * FXML Controller class
 *
 * @author Andreas
 */
public class CacheDetailsController implements Initializable, SessionContextListener {

  @FXML
  private AnchorPane cacheDetailPane;
  @FXML
  private Label cacheNameHeader;
  @FXML
  private Label detailsIconHeader;
  @FXML
  private TextField cacheName;
  @FXML
  private Slider difficultySlider;
  @FXML
  private Slider terrainSlider;
  @FXML
  private TextField placedByTextfield;
  @FXML
  private TextField ownerTextfield;
  @FXML
  private MapPaneWrapper mapPaneWrapper;
  @FXML
  private DatePicker date;
  @FXML
  private TextField typeTextfield;
  @FXML
  private TextField containerTextfield;
  @FXML
  private BorderPane shortDescriptionPane;
  @FXML
  private BorderPane longDescriptionPane;
  @FXML
  private CheckBox shortDescriptionHtml;
  @FXML
  private CheckBox longDescriptionHtml;

  private ImageView icon;
  private FadeTransition ft;
  private TextArea shortDescriptionField;
  private TextArea longDescriptionField;
  private WebView shortDescriptionWebView;
  private WebView longDescriptionWebView;

  private final SessionContext sessionContext = SessionContext.getInstance();

  /**
   * Initializes the controller class.
   *
   * @param url
   * @param rb
   */
  @Override
  public void initialize(URL url, ResourceBundle rb) {
    setSessionListener();
    cacheNameHeader.textProperty().bind(cacheName.textProperty());
    detailsIconHeader.getStyleClass().add("cache-list-icon");
    icon = new ImageView();
    detailsIconHeader.setGraphic(icon);
    cacheDetailPane.setOpacity(0.3);
    shortDescriptionWebView = new WebView();
    longDescriptionWebView = new WebView();
    shortDescriptionField = new TextArea();
    longDescriptionField = new TextArea();
    editableForm(false);
    initFading();
  }


  @Override
  public void sessionContextChanged() {
    Cache currentCache = (Cache) sessionContext.getData("current-cache");
    if (currentCache != null) {
      fillForm(currentCache);
      fadeIn();
    } else {
      resetForm();
      fadeOut();
    }
  }

  private void setSessionListener() {
    sessionContext.addListener("current-cache", this);
  }

  private void initFading() {
    ft = new FadeTransition(Duration.millis(1000), cacheDetailPane);
    ft.setCycleCount(1);
    ft.setAutoReverse(false);
  }

  private void fadeIn() {
    ft.setFromValue(0.3);
    ft.setToValue(1.0);
    ft.playFromStart();
  }

  private void fadeOut() {
    ft.setFromValue(1.0);
    ft.setToValue(0.3);
    ft.playFromStart();
  }

  private void resetForm() {
    cacheName.setText("");
    difficultySlider.setValue(1.0);
    terrainSlider.setValue(1.0);
    detailsIconHeader.setText("");
    placedByTextfield.setText("");
    ownerTextfield.setText("");
    date.setValue(LocalDate.now());
    typeTextfield.setText("");
    containerTextfield.setText("");
    shortDescriptionPane.setCenter(null);
    longDescriptionPane.setCenter(null);
//            final double lat = currentCache.getMainWayPoint().getLatitude();
//            final double lon = currentCache.getMainWayPoint().getLongitude();
//            mapPaneWrapper.setDisplayPositionByLatLon(lat, lon, 15);
//
//            MapMarkable marker = new ImageMapMarker(GeocachingIcons.getMapIcon(currentCache, IconManager.IconSize.BIG), lat, lon);
//            mapPaneWrapper.setMapMarkerList(Arrays.asList(marker));        
  }

  private void fillForm(Cache currentCache) throws NumberFormatException {
    cacheName.setText(currentCache.getName());
    difficultySlider.setValue(Double.parseDouble(currentCache.getDifficulty()));
    terrainSlider.setValue(Double.parseDouble(currentCache.getTerrain()));
    icon.setImage(GeocachingIcons.getIcon(currentCache));
    placedByTextfield.setText(currentCache.getPlacedBy());
    ownerTextfield.setText(currentCache.getOwner().getName());
    date.setValue(currentCache.getMainWayPoint().getTime().toLocalDate());
    typeTextfield.setText(currentCache.getType());
    containerTextfield.setText(currentCache.getContainer());
    fillShortDescription(currentCache);
    fillLongDescription(currentCache);

    fillMap(currentCache);
  }

  private void fillMap(Cache currentCache) {
    final double lat = currentCache.getMainWayPoint().getLatitude();
    final double lon = currentCache.getMainWayPoint().getLongitude();
    mapPaneWrapper.setDisplayPositionByLatLon(lat, lon, 15);

    MapMarkable marker = new ImageMapMarker(GeocachingIcons.getMapIcon(currentCache, IconManager.IconSize.BIG), lat, lon);
    mapPaneWrapper.setMapMarkerList(Arrays.asList(marker));
  }

  private void fillShortDescription(Cache currentCache) {
    Node shortDescriptionNode;
    if (currentCache.isShortDescriptionHtml()) {
      final WebEngine webEngine = shortDescriptionWebView.getEngine();
      webEngine.loadContent(currentCache.getShortDescription());
      shortDescriptionNode = shortDescriptionWebView;
    } else {
      shortDescriptionField.setText(currentCache.getShortDescription());
      shortDescriptionNode = shortDescriptionField;
    }
    shortDescriptionPane.setCenter(shortDescriptionNode);
    shortDescriptionHtml.setSelected(currentCache.isShortDescriptionHtml());
  }

  private void fillLongDescription(Cache currentCache) {
    Node longDescriptionNode;
    if (currentCache.isLongDescriptionHtml()) {
      final WebEngine webEngine = longDescriptionWebView.getEngine();
      webEngine.loadContent(currentCache.getLongDescription());
      longDescriptionNode = longDescriptionWebView;
    } else {
      longDescriptionField.setText(currentCache.getLongDescription());
      longDescriptionNode = longDescriptionField;
    }
    longDescriptionPane.setCenter(longDescriptionNode);
    longDescriptionHtml.setSelected(currentCache.isLongDescriptionHtml());
  }

  private void editableForm(boolean editable) {
    cacheName.setEditable(editable);
    difficultySlider.setDisable(!editable);
    terrainSlider.setDisable(!editable);
    placedByTextfield.setEditable(editable);
    ownerTextfield.setEditable(editable);
    date.setDisable(!editable);
    typeTextfield.setEditable(editable);
    containerTextfield.setEditable(editable);
    shortDescriptionHtml.setDisable(!editable);
    longDescriptionHtml.setDisable(!editable);
    shortDescriptionField.setEditable(editable);
    longDescriptionField.setEditable(editable);
  }

}
