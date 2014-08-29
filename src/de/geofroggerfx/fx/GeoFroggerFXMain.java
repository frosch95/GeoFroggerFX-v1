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
package de.geofroggerfx.fx;

import de.geofroggerfx.fx.utils.ApplicationParametersProvider;
import de.geofroggerfx.fx.utils.StartupScene;
import de.geofroggerfx.database.DatabaseService;
import javafx.application.Application;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import org.jboss.weld.environment.se.Weld;
import org.jboss.weld.environment.se.WeldContainer;

import javax.enterprise.util.AnnotationLiteral;

/**
 * @author Andreas
 */
public class GeoFroggerFXMain extends Application {

  private Weld weld;
  private WeldContainer weldContainer;

  @Override
  public void init() throws Exception {
    super.init();

    // Init Weld CDI
    weld = new Weld();
  }

  @Override
  public void start(Stage stage) throws Exception {
    loadCustomFonts();

    weldContainer = weld.initialize();

    // Make the application parameters injectable with a standard CDI
    // annotation
    weldContainer.instance().select(ApplicationParametersProvider.class).get().setParameters(getParameters());

    // Now that JavaFX thread is ready
    // let's inform whoever cares using standard CDI notification mechanism:
    // CDI events
    weldContainer.event().select(Stage.class, new AnnotationLiteral<StartupScene>() {}).fire(stage);
  }

  private void loadCustomFonts() {
    Font.loadFont(GeoFroggerFXMain.class.getResource("/fonts/FiraMonoOT-Bold.otf").toExternalForm(), 12);
    Font.loadFont(GeoFroggerFXMain.class.getResource("/fonts/FiraMonoOT-Regular.otf").toExternalForm(), 12);
    Font.loadFont(GeoFroggerFXMain.class.getResource("/fonts/FiraSansOT-Bold.otf").toExternalForm(), 12);
    Font.loadFont(GeoFroggerFXMain.class.getResource("/fonts/FiraSansOT-BoldItalic.otf").toExternalForm(), 12);
    Font.loadFont(GeoFroggerFXMain.class.getResource("/fonts/FiraSansOT-Light.otf").toExternalForm(), 12);
    Font.loadFont(GeoFroggerFXMain.class.getResource("/fonts/FiraSansOT-LightItalic.otf").toExternalForm(), 12);
    Font.loadFont(GeoFroggerFXMain.class.getResource("/fonts/FiraSansOT-Medium.otf").toExternalForm(), 12);
    Font.loadFont(GeoFroggerFXMain.class.getResource("/fonts/FiraSansOT-MediumItalic.otf").toExternalForm(), 12);
    Font.loadFont(GeoFroggerFXMain.class.getResource("/fonts/FiraSansOT-Regular.otf").toExternalForm(), 12);
    Font.loadFont(GeoFroggerFXMain.class.getResource("/fonts/FiraSansOT-RegularItalic.otf").toExternalForm(), 12);
  }

  @Override
  public void stop() throws Exception {
    weldContainer.instance().select(DatabaseService.class).get().close();
    weld.shutdown();
    super.stop();
  }


  /**
   * The main() method is ignored in correctly deployed JavaFX application.
   * main() serves only as fallback in case the application can not be
   * launched through deployment artifacts, e.g., in IDEs with limited FX
   * support. NetBeans ignores main().
   *
   * @param args the command line arguments
   */
  public static void main(String[] args) {
    launch(args);
  }
}
