/*
 * Copyright (c) 2014, Andreas Billmann <abi@geofroggerfx.de>
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
