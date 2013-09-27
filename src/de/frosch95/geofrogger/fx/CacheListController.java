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
import de.frosch95.geofrogger.fx.components.CacheListCell;
import de.frosch95.geofrogger.model.Cache;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.util.Callback;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import static de.frosch95.geofrogger.fx.JavaFXUtils.addClasses;

/**
 * FXML Controller class
 *
 * @author Andreas
 */
public class CacheListController implements Initializable, SessionContextListener {

  private static final String CACHE_LIST_ACTION_ICONS = "cache-list-action-icons";
  private final SessionContext sessionContext = SessionContext.getInstance();

  @FXML
  private ListView cacheListView;

  @FXML
  private Label cacheNumber;

  @FXML
  private Label filterIcon;

  @FXML
  private Label sortIcon;

  /**
   * Initializes the controller class.
   *
   * @param url
   * @param rb
   */
  @Override
  public void initialize(URL url, ResourceBundle rb) {
    setSessionListener();
    setCellFactory();

    cacheListView.getSelectionModel().selectedItemProperty().addListener(
        (ChangeListener<Cache>) (ObservableValue<? extends Cache> ov, Cache oldValue, Cache newValue) ->
            sessionContext.setData("current-cache", newValue)
    );

    addClasses(filterIcon, CACHE_LIST_ACTION_ICONS);
    addClasses(sortIcon, CACHE_LIST_ACTION_ICONS);
  }

  @Override
  public void sessionContextChanged() {
    List<Cache> caches = (List<Cache>) sessionContext.getData("cache-list");
    Platform.runLater(() -> {
      cacheNumber.setText("(" + caches.size() + ")");
      cacheListView.getItems().setAll(caches);
    });
  }

  private void setCellFactory() {
    cacheListView.setCellFactory(
        (Callback<ListView<Cache>, CacheListCell>)
        (ListView<Cache> p) -> new CacheListCell());
  }

  private void setSessionListener() {
    sessionContext.addListener("cache-list", this);
  }

}
