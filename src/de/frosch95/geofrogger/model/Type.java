package de.frosch95.geofrogger.model;

/**
 * This enum represents the different cache types
 *
 * @author abi
 */
public enum Type {

  TRADITIONAL_CACHE("Traditional Cache"),
  MULTI_CACHE("Multi-cache"),
  UNKNOWN_CACHE("Unknown Cache"),
  EARTH_CACHE("Earthcache"),
  LETTERBOX("Letterbox Hybrid"),
  EVENT("Event Cache"),
  WHERIGO("Wherigo Cache"),
  WEBCAM_CACHE("Webcam Cache"),
  VIRTUAL_CACHE("Virtual Cache"),
  CITO_EVENT("Cache In Trash Out Event"),
  MEGA_EVENT("Mega-Event Cache");

  private String groundspeakString;

  private Type(String groundspeakString) {
    this.groundspeakString = groundspeakString;
  }

  public String toGroundspeakString() {
    return groundspeakString;
  }

  public static Type groundspeakStringToType(String groundspeakString) {
    for (Type t: Type.values()) {
      if (t.toGroundspeakString().equals(groundspeakString)) {
        return t;
      }
    }
    throw new IllegalArgumentException("unknown type:"+groundspeakString);
  }

}
