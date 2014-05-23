package de.geofroggerfx.fx.utils;

import javax.inject.Qualifier;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This class is based on the tutorial
 * http://blog.matthieu.brouillard.fr/2012/08/fxml-javafx-powered-by-cdi-jboss-weld_6.html
 *
 * Thanks to Matthieu BROUILLARD
 */
@Qualifier
@Target({ElementType.FIELD, ElementType.PARAMETER, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface StartupScene {
}
