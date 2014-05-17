package de.geofroggerfx.model;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * This class represents the application settings, like current user, etc.
 *
 * @author abi
 */
@Entity
public class Settings {

  @Id
  private Long id;

  private Long currentUserID = new Long(3906456);

  public Long getCurrentUserID() {
    return currentUserID;
  }

  public void setCurrentUserID(final Long currentUserID) {
    this.currentUserID = currentUserID;
  }
}
