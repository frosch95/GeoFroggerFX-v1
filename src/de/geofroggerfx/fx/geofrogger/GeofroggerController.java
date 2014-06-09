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
package de.geofroggerfx.fx.geofrogger;

import de.geofroggerfx.application.ProgressEvent;
import de.geofroggerfx.application.SessionContext;
import de.geofroggerfx.gpx.GPXReader;
import de.geofroggerfx.model.Cache;
import de.geofroggerfx.plugins.Plugin;
import de.geofroggerfx.plugins.PluginService;
import de.geofroggerfx.service.CacheService;
import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import org.controlsfx.dialog.Dialog;

import javax.inject.Inject;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import static de.geofroggerfx.service.CacheSortField.NAME;
import static de.geofroggerfx.service.SortDirection.ASC;

/**
 * FXML Controller class
 *
 * @author Andreas
 */
public class GeofroggerController implements Initializable {

  private static final String LICENSE = "/*\n" +
      " * Copyright (c) 2013, Andreas Billmann <abi@geofroggerfx.de>\n" +
      " * All rights reserved.\n" +
      " *\n" +
      " * Redistribution and use in source and binary forms, with or without\n" +
      " * modification, are permitted provided that the following conditions are met:\n" +
      " *\n" +
      " * * Redistributions of source code must retain the above copyright notice, this\n" +
      " *   list of conditions and the following disclaimer.\n" +
      " * * Redistributions in binary form must reproduce the above copyright notice,\n" +
      " *   this list of conditions and the following disclaimer in the documentation\n" +
      " *   and/or other materials provided with the distribution.\n" +
      " *\n" +
      " * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS \"AS IS\"\n" +
      " * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE\n" +
      " * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE\n" +
      " * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE\n" +
      " * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR\n" +
      " * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF\n" +
      " * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS\n" +
      " * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN\n" +
      " * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)\n" +
      " * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE\n" +
      " * POSSIBILITY OF SUCH DAMAGE.\n" +
      " */";

  private static final String MASTHEAD_TEXT = "GeoFroggerFX by Andreas Billmann <abi@geofroggerfx.de>";
  private static final String ABOUT_TEXT = "Used libs:\n"
      + "\t- JFXtras 8.0 r1\n"
      + "\t- ControlsFX 8.0.2 developer preview 1\n"
      + "\t- jdom 2.x\n"
      + "\t- H2 1.3.173\n"
      + "\t- Icons by http://iconmonstr.com/\n";

  @Inject
  private SessionContext sessionContext;
  private final LoadCachesFromFileService loadService = new LoadCachesFromFileService();
  private final LoadCachesFromDatabaseService loadFromDBService = new LoadCachesFromDatabaseService();
  private final LoadCacheListsFromDatabaseService loadListsFromDBService = new LoadCacheListsFromDatabaseService();

  @Inject
  private GPXReader gpxReader;

  @Inject
  private CacheService cacheService;

  @Inject
  private PluginService pluginService;

  @FXML
  private Label leftStatus;

  @FXML
  private ProgressBar progress;

  @FXML
  private Menu pluginsMenu;

  @FXML
  private BorderPane mainPane;

  @FXML
  private SplitPane contentPane;


  /**
   * Initializes the controller class.
   *
   * @param url
   * @param rb
   */
  @Override
  public void initialize(URL url, ResourceBundle rb) {

    List<Plugin> plugins = pluginService.getAllPlugins();
    for (Plugin plugin: plugins) {
      MenuItem menuItem = new MenuItem(plugin.getName()+" ("+plugin.getVersion()+")");
      menuItem.setOnAction(actionEvent -> pluginService.executePlugin(plugin));
      pluginsMenu.getItems().add(menuItem);
    }

    gpxReader.addListener((ProgressEvent event) -> updateStatus(event.getMessage(), event.getProgress()));
    cacheService.addListener((ProgressEvent event) -> updateStatus(event.getMessage(), event.getProgress()));
    loadListsFromDBService.start();
    loadFromDBService.start();
  }

  @FXML
  public void importGpx(ActionEvent actionEvent) {
    final FileChooser fileChooser = new FileChooser();

    //Set extension filter
    final FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("GPX files (*.gpx)", "*.gpx");
    fileChooser.getExtensionFilters().add(extFilter);

    //Show open file dialog
    final File file = fileChooser.showOpenDialog(null);
    loadService.setFile(file);
    loadService.restart();
  }

  @FXML
  public void showAboutDialog(ActionEvent actionEvent) {
    Dialog dialog = new Dialog(null, "About");
    dialog.setMasthead(MASTHEAD_TEXT);
    dialog.setContent(ABOUT_TEXT);
    dialog.setExpandableContent(new TextArea(LICENSE));
    dialog.show();
  }

  @FXML
  public void showSettings(ActionEvent actionEvent) {
//    mainPane.setCenter();

//    FXMLLoader.load()
  }

  @FXML
  public void exit(ActionEvent actionEvent) {
    Platform.exit();
  }

  private void updateStatus(String text, double progressValue) {
    Platform.runLater(() -> {
      leftStatus.setText(text);
      progress.setProgress(progressValue);
    });
  }


  private class LoadCacheListsFromDatabaseService extends Service {

    @Override
    protected Task createTask() {
      return new Task() {
        @Override
        protected Void call() throws Exception {
          updateStatus("Load cache lists from database.", ProgressIndicator.INDETERMINATE_PROGRESS);
          sessionContext.setData("cache-lists", cacheService.getAllCacheLists());
          updateStatus("All cache lists loaded.", 0);
          return null;
        }
      };
    }

  }


  private class LoadCachesFromDatabaseService extends Service {

    @Override
    protected Task createTask() {
      return new Task() {
        @Override
        protected Void call() throws Exception {
          updateStatus("Load caches from database.", ProgressIndicator.INDETERMINATE_PROGRESS);
          sessionContext.setData("cache-list", cacheService.getAllCaches(NAME, ASC));
          updateStatus("All caches loaded.", 0);
          return null;
        }
      };
    }

  }

  private class LoadCachesFromFileService extends Service {

    private final ObjectProperty<File> file = new SimpleObjectProperty();

    public final void setFile(File value) {
      file.set(value);
    }

    public final File getFile() {
      return file.get();
    }

    public final ObjectProperty<File> fileProperty() {
      return file;
    }

    @Override
    protected Task createTask() {
      return new Task() {
        @Override
        protected Void call() throws Exception {
          try {
            final List<Cache> cacheList = gpxReader.load(file.get().getAbsolutePath());
            if (cacheList != null && !cacheList.isEmpty()) {

              updateStatus("Store caches in database", ProgressIndicator.INDETERMINATE_PROGRESS);
              cacheService.storeCaches(cacheList);
              updateStatus("All caches are stored in database", 0);

              updateStatus("Load caches from database.", ProgressIndicator.INDETERMINATE_PROGRESS);
              sessionContext.setData("cache-list", cacheService.getAllCaches(NAME, ASC));
              updateStatus("All caches loaded.", 0);
            }
          } catch (IOException ex) {
            Logger.getLogger(GeofroggerController.class.getName()).log(Level.SEVERE, null, ex);
          }
          return null;
        }
      };
    }

  }

}
