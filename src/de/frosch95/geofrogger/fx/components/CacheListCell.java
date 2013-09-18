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
import de.frosch95.geofrogger.model.CacheUtils;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.image.ImageView;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;

import static de.frosch95.geofrogger.fx.JavaFXUtils.addClasses;
import static de.frosch95.geofrogger.fx.JavaFXUtils.removeClasses;

/**
 * @author Andreas
 */
public class CacheListCell extends ListCell<Cache> {
  private static final String CACHE_LIST_FOUND_CLASS = "cache-list-found";
  private static final String CACHE_LIST_NOT_FOUND_CLASS = "cache-list-not-found";
  private static final String CACHE_LIST_NAME_CLASS = "cache-list-name";
  private static final String CACHE_LIST_DT_CLASS = "cache-list-dt";
  private static final String CACHE_LIST_ICON_CLASS = "cache-list-icon";

  private final GridPane grid = new GridPane();
  private final ImageView icon = new ImageView();
  private final Label name = new Label();
  private final Label dt = new Label();
  private final ImageView foundIcon = new ImageView();
  private final ImageView favoriteIcon = new ImageView();

  public CacheListCell() {
    configureGrid();
    configureIcon();
    configureName();
    configureDifficultyTerrain();
    addControlsToGrid();
  }

  @Override
  public void updateItem(Cache cache, boolean empty) {
    super.updateItem(cache, empty);
    if (empty) {
      clearContent();
    } else {
      addContent(cache);
    }
  }

  private void configureGrid() {
    grid.setHgap(10);
    grid.setVgap(4);
    grid.setPadding(new Insets(0, 10, 0, 10));

    ColumnConstraints column1 = new ColumnConstraints(32);
    ColumnConstraints column2 = new ColumnConstraints(100, 100, Double.MAX_VALUE);
    column2.setHgrow(Priority.ALWAYS);
    ColumnConstraints column3 = new ColumnConstraints(32);
    grid.getColumnConstraints().addAll(column1, column2, column3);
  }

  private void configureIcon() {
    icon.getStyleClass().add(CACHE_LIST_ICON_CLASS);
  }

  private void configureName() {
    name.getStyleClass().add(CACHE_LIST_NAME_CLASS);
  }

  private void configureDifficultyTerrain() {
    dt.getStyleClass().add(CACHE_LIST_DT_CLASS);
  }

  private void addControlsToGrid() {
    grid.add(icon, 0, 0, 1, 2);
    grid.add(name, 1, 0);
    grid.add(dt, 1, 1);
    grid.add(foundIcon, 2, 0);
    grid.add(favoriteIcon, 2, 1);
  }

  private void clearContent() {
    setText(null);
    setGraphic(null);
  }

  private void addContent(Cache cache) {
    setText(null);
    icon.setImage(GeocachingIcons.getIcon(cache));
    name.setText(cache.getName());
    dt.setText("D: " + cache.getDifficulty() + " / T:" + cache.getTerrain());

    if (CacheUtils.hasUserFoundCache(cache, new Long(3906456))) {
      foundIcon.setImage(IconManager.getIcon("/icons/iconmonstr-check-mark-11-icon.png", IconManager.IconSize.SMALL));
    } else {
      foundIcon.setImage(null);
    }

    setStyleClassDependingOnFoundState(cache);
    setGraphic(grid);
  }

  private void setStyleClassDependingOnFoundState(Cache cache) {
    if (CacheUtils.hasUserFoundCache(cache, new Long(3906456))) {
      addClasses(this, CACHE_LIST_FOUND_CLASS);
      removeClasses(this, CACHE_LIST_NOT_FOUND_CLASS);
    } else {
      addClasses(this, CACHE_LIST_NOT_FOUND_CLASS);
      removeClasses(this, CACHE_LIST_FOUND_CLASS);
    }
  }
}
