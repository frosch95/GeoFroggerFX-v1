package de.geofroggerfx.fx.utils;

import javafx.application.Application;

import javax.enterprise.inject.Produces;
import javax.inject.Singleton;

/**
 * This class is based on the tutorial
 * http://blog.matthieu.brouillard.fr/2012/08/fxml-javafx-powered-by-cdi-jboss-weld_6.html
 *
 * Thanks to Matthieu BROUILLARD
 */
@Singleton
public class ApplicationParametersProvider {
  private Application.Parameters parameters;

  public void setParameters(Application.Parameters p) {
    this.parameters = p;
  }

  public @Produces Application.Parameters getParameters() {
    return parameters;
  }
}
