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
package de.geofroggerfx.fx.cachelist;

import de.geofroggerfx.application.SessionConstants;
import de.geofroggerfx.application.SessionContext;
import de.geofroggerfx.fx.components.CacheListCell;
import de.geofroggerfx.fx.components.IconManager;
import de.geofroggerfx.fx.components.SortingMenuItem;
import de.geofroggerfx.model.Cache;
import de.geofroggerfx.model.CacheList;
import de.geofroggerfx.service.CacheService;
import de.geofroggerfx.service.CacheSortField;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.util.Callback;
import org.scenicview.ScenicView;

import javax.inject.Inject;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import static de.geofroggerfx.application.SessionConstants.*;
import static de.geofroggerfx.fx.utils.JavaFXUtils.addClasses;
import static de.geofroggerfx.service.CacheSortField.*;

/**
 * FXML Controller class
 *
 * @author Andreas
 */
public class CacheListController implements Initializable {

  private static final String CACHE_LIST_ACTION_ICONS = "cache-list-action-icons";

  private ResourceBundle resourceBundle;

  @Inject
  private SessionContext sessionContext;

  @Inject
  private CacheService cacheService;

  private SortingMenuItem currentSortingButton = null;

  @FXML
  private ListView cacheListView;

  @FXML
  private Label cacheNumber;

  @FXML
  private MenuButton menuIcon;

  @FXML
  private ComboBox cacheListComboBox;

  /**
   * Initializes the controller class.
   *
   * @param url
   * @param rb
   */
  @Override
  @SuppressWarnings("unchecked")
  public void initialize(URL url, ResourceBundle rb) {
    this.resourceBundle = rb;

    setSessionListener();
    setCellFactory();

    cacheListView.getSelectionModel().selectedItemProperty().addListener(
        (ChangeListener<Cache>) (ObservableValue<? extends Cache> ov, Cache oldValue, Cache newValue) ->
            sessionContext.setData(CURRENT_CACHE, newValue)
    );

    initCacheListComboBox();
    initListMenuButton();
  }

  @SuppressWarnings("unchecked")
  private void setCellFactory() {
    cacheListView.setCellFactory(
        (Callback<ListView<Cache>, CacheListCell>)
        (ListView<Cache> p) -> new CacheListCell());
  }

  private void setSessionListener() {
    sessionContext.addListener(CACHE_LIST, () -> Platform.runLater(this::resetCacheList));
    sessionContext.addListener(CACHE_LISTS, () -> Platform.runLater(this::refreshCacheListComboAndSelectFirst));
  }

  @SuppressWarnings("unchecked")
  private void resetCacheList() {
    List<Cache> caches = (List<Cache>) sessionContext.getData(CACHE_LIST);
    cacheNumber.setText(" " + caches.size());
    cacheListView.getItems().setAll(caches);
  }

  @SuppressWarnings("unchecked")
  private void refreshCacheListComboAndSelectFirst() {
    List<CacheList> cacheLists = (List<CacheList>) sessionContext.getData(CACHE_LISTS);
    cacheListComboBox.getItems().clear();
    cacheListComboBox.getItems().add(resourceBundle.getString("all.caches"));
    cacheListComboBox.getItems().addAll(cacheLists);
    cacheListComboBox.getSelectionModel().selectFirst();
  }


  private void initCacheListComboBox() {
    cacheListComboBox.setOnAction(actionEvent -> {
      final Object selectedItem = cacheListComboBox.getSelectionModel().getSelectedItem();
      Platform.runLater(() -> cacheListSelectAction(selectedItem));
    });
  }

  private void cacheListSelectAction(Object selectedItem) {
    if (selectedItem != null) {
      if (selectedItem.equals(resourceBundle.getString("all.caches"))) {
        loadAllCaches();
      } else{
        CacheList cacheList = (CacheList)selectedItem;
        sessionContext.setData(CACHE_LIST, cacheList.getCaches());
      }
    } else {
      cacheListComboBox.getSelectionModel().selectFirst();
    }
  }

  private void initListMenuButton() {
    addClasses(menuIcon, CACHE_LIST_ACTION_ICONS);
    menuIcon.setGraphic(new ImageView(IconManager.getIcon("iconmonstr-menu-icon.png", IconManager.IconSize.SMALL)));
    menuIcon.getItems().addAll(createSortMenu());
    menuIcon.minHeightProperty().bind(cacheListComboBox.heightProperty());
    menuIcon.prefHeightProperty().bind(cacheListComboBox.heightProperty());
    menuIcon.maxHeightProperty().bind(cacheListComboBox.heightProperty());
  }

  private Menu createSortMenu() {
    Menu sortMenu = new Menu(resourceBundle.getString("menu.title.sort"));
    currentSortingButton = createSortButton(NAME);
    currentSortingButton.setSelected(true);
    sortMenu.getItems().addAll(
        currentSortingButton,
        createSortButton(TYPE),
        createSortButton(DIFFICULTY),
        createSortButton(TERRAIN),
        createSortButton(OWNER),
        createSortButton(PLACEDBY));
    return sortMenu;
  }

  private SortingMenuItem createSortButton(final CacheSortField field) {
    SortingMenuItem button = new SortingMenuItem(resourceBundle.getString("sort.cache."+field.getFieldName()), field);
    button.setOnAction(actionEvent -> {

      // if there was another button selected, change the selection
      if (!currentSortingButton.equals(button)) {
        currentSortingButton.setSelected(false);
        currentSortingButton = button;
      }

      currentSortingButton.setSelected(true);
      loadAllCaches();
    });
    return button;
  }

  private void loadAllCaches() {
    sessionContext.setData(CACHE_LIST, cacheService.getAllCaches(currentSortingButton.getField(), currentSortingButton.getSortDirection()));
  }

}
