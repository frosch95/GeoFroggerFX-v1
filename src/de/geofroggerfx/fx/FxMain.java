package de.geofroggerfx.fx;

import de.geofroggerfx.fx.utils.StartupScene;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import org.scenicview.ScenicView;

import javax.enterprise.event.Observes;
import javax.inject.Inject;
import java.io.IOException;
import java.io.InputStream;
import java.util.ResourceBundle;

/**
 * This class is based on the tutorial
 * http://blog.matthieu.brouillard.fr/2012/08/fxml-javafx-powered-by-cdi-jboss-weld_6.html
 *
 * Thanks to Matthieu BROUILLARD
 */
public class FxMain {

  @Inject
  private FXMLLoader fxmlLoader;

  public void start( @Observes @StartupScene Stage stage) throws IOException
  {
    try (InputStream fxml = getClass().getResourceAsStream( "geofrogger/geofrogger.fxml" )) {
      fxmlLoader.setResources(ResourceBundle.getBundle("de.geofroggerfx.fx.geofrogger"));
      final Parent root = fxmlLoader.load(fxml);
      final Scene scene = new Scene(root);
      stage.setScene(scene);
      stage.show();

      scene.setOnKeyPressed(keyEvent -> {
        if (isScenicViewShortcutPressed(keyEvent)) {
          ScenicView.show(scene);
        }
      });
    }
  }

  private boolean isScenicViewShortcutPressed(final KeyEvent keyEvent) {
    return keyEvent.isAltDown() && keyEvent.isControlDown() && keyEvent.getCode().equals(KeyCode.V);
  }
}
