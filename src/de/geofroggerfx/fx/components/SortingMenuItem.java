package de.geofroggerfx.fx.components;

import de.geofroggerfx.service.CacheSortField;
import de.geofroggerfx.service.SortDirection;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.MenuItem;

import static de.geofroggerfx.service.SortDirection.ASC;
import static de.geofroggerfx.service.SortDirection.DESC;

/**
 * MenuItem with a ASC and DESC arrow when selected
 */
public class SortingMenuItem extends MenuItem {

  private static final String BLACK_UP_POINTING_TRIANGLE = " \u25B2";
  private static final String BLACK_DOWN_POINTING_TRIANGLE = " \u25BC";

  private boolean oldValue = false;
  private ObjectProperty<CacheSortField> field = new SimpleObjectProperty<>();
  private ObjectProperty<SortDirection> sortDirection = new SimpleObjectProperty<>(ASC);

  public SortingMenuItem(final String text, final CacheSortField field) {
    super(text);
    this.field.setValue(field);
  }

  public void setSelected(boolean newValue) {
    final StringBuilder text = new StringBuilder(getText());
    String triangle = BLACK_UP_POINTING_TRIANGLE;

    if (newValue && oldValue) {
      sortDirection.set(sortDirection.get().equals(ASC) ? DESC : ASC);
      triangle = sortDirection.get().equals(ASC) ? BLACK_UP_POINTING_TRIANGLE : BLACK_DOWN_POINTING_TRIANGLE;
      text.delete(text.length() - 2, text.length());
      text.append(triangle);
    } else if (newValue) {
      text.append(triangle);
    } else {
      text.delete(text.length() - 2, text.length());
    }

    setText(text.toString());
    oldValue = newValue;
  }

  public CacheSortField getField() {
    return field.get();
  }

  public SortDirection getSortDirection() {
    return sortDirection.get();
  }
}
