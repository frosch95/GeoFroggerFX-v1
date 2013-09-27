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
package de.frosch95.geofrogger.fx.cachelist;

import de.frosch95.geofrogger.application.ServiceManager;
import de.frosch95.geofrogger.application.SessionContext;
import de.frosch95.geofrogger.application.SessionContextListener;
import de.frosch95.geofrogger.fx.components.CacheListCell;
import de.frosch95.geofrogger.fx.components.IconManager;
import de.frosch95.geofrogger.model.Cache;
import de.frosch95.geofrogger.service.CacheService;
import de.frosch95.geofrogger.service.CacheSortField;
import de.frosch95.geofrogger.service.SortDirection;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.util.Callback;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import static de.frosch95.geofrogger.fx.utils.JavaFXUtils.addClasses;
import static de.frosch95.geofrogger.service.CacheSortField.*;
import static de.frosch95.geofrogger.service.SortDirection.*;

/**
 * FXML Controller class
 *
 * @author Andreas
 */
public class CacheListController implements Initializable, SessionContextListener {

  private static final String CACHE_LIST_ACTION_ICONS = "cache-list-action-icons";
  private final SessionContext sessionContext = SessionContext.getInstance();
  private final CacheService cacheService = ServiceManager.getInstance().getCacheService();
  private CacheSortField currentSortField = NAME;
  private SortDirection currentSortDirection = ASC;

  @FXML
  private ListView cacheListView;

  @FXML
  private Label cacheNumber;

  @FXML
  private MenuButton menuIcon;

  /**
   * Initializes the controller class.
   *
   * @param url
   * @param rb
   */
  @Override
  @SuppressWarnings("unchecked")
  public void initialize(URL url, ResourceBundle rb) {
    setSessionListener();
    setCellFactory();

    cacheListView.getSelectionModel().selectedItemProperty().addListener(
        (ChangeListener<Cache>) (ObservableValue<? extends Cache> ov, Cache oldValue, Cache newValue) ->
            sessionContext.setData("current-cache", newValue)
    );

    initListMenuButton(rb);
  }

  @Override
  @SuppressWarnings("unchecked")
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

  private void initListMenuButton(final ResourceBundle rb) {
    addClasses(menuIcon, CACHE_LIST_ACTION_ICONS);
    menuIcon.setGraphic(new ImageView(IconManager.getIcon("/icons/iconmonstr-menu-icon.png", IconManager.IconSize.SMALL)));
    menuIcon.getItems().addAll(createSortMenu(rb));
  }

  private Menu createSortMenu(final ResourceBundle rb) {
    Menu sortMenu = new Menu(rb.getString("menu.title.sort"));
    sortMenu.getItems().addAll(
        createSortButton(rb, NAME),
        createSortButton(rb, TYPE),
        createSortButton(rb, DIFFICULTY),
        createSortButton(rb, TERRAIN),
        createSortButton(rb, OWNER),
        createSortButton(rb, PLACEDBY));
    return sortMenu;
  }

  private MenuItem createSortButton(final ResourceBundle rb, final CacheSortField field) {
    MenuItem button = new MenuItem(rb.getString("sort.cache."+field.getFieldName()));
    button.setOnAction(actionEvent -> {
      currentSortDirection = (field.equals(currentSortField) && currentSortDirection.equals(ASC)) ? DESC : ASC;
      currentSortField = field;
      sessionContext.setData("cache-list", cacheService.getAllCaches(currentSortField, currentSortDirection));
    });
    return button;
  }


}
