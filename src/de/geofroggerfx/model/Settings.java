package de.geofroggerfx.model;

/**
 * This class represents the application settings, like current user, etc.
 *
 * @author abi
 */
public class Settings {

  private Long id;

  private Long currentUserID = new Long(3906456);

  public Long getCurrentUserID() {
    return currentUserID;
  }

  public void setCurrentUserID(final Long currentUserID) {
    this.currentUserID = currentUserID;
  }
}
