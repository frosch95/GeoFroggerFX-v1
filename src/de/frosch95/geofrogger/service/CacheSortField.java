package de.frosch95.geofrogger.service;

/**
 * Sort fields on cache object
 *
 * @author abi
 */
public enum CacheSortField {
  NAME("name"),
  TYPE("type"),
  DIFFICULTY("difficulty"),
  TERRAIN("terrain"),
  PLACEDBY("placedBy"),
  OWNER("owner");

  private String fieldName;

  private CacheSortField(String fieldName) {
    this.fieldName = fieldName;
  }

  public String getFieldName() {
    return fieldName;
  }
}
