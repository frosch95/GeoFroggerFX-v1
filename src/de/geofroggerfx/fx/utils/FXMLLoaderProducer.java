package de.geofroggerfx.fx.utils;

import javafx.fxml.FXMLLoader;

import javax.enterprise.inject.Instance;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;

/**
 * This class is based on the tutorial
 * http://blog.matthieu.brouillard.fr/2012/08/fxml-javafx-powered-by-cdi-jboss-weld_6.html
 *
 * Thanks to Matthieu BROUILLARD
 */
public class FXMLLoaderProducer {

  @Inject
  private Instance<Object> instance;

  @Produces
  public FXMLLoader createLoader()
  {
    final FXMLLoader loader = new FXMLLoader();
    loader.setControllerFactory(param -> instance.select(param).get());
    return loader;
  }
}
