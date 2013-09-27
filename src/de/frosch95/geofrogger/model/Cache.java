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
package de.frosch95.geofrogger.model;

import javax.persistence.*;
import java.util.List;

/**
 * @author Andreas Billmann
 */
@Entity
public class Cache {

  @Id
  private Long id;
  private boolean available;
  private boolean archived;
  private String name;
  private String placedBy;
  private User owner;
  private Type type;
  private String container;
  private List<Attribute> attributes;
  private String difficulty;
  private String terrain;
  private String country;
  private String state;
  private String shortDescription;
  private boolean shortDescriptionHtml;
  private String longDescription;
  private boolean longDescriptionHtml;
  private String encodedHints;
  private List<Log> logs;
  private List<TravelBug> travelBugs;
  private Waypoint mainWayPoint;

  @OneToOne(fetch=FetchType.LAZY)
  public Waypoint getMainWayPoint() {
    return mainWayPoint;
  }

  public void setMainWayPoint(Waypoint mainWayPoint) {
    this.mainWayPoint = mainWayPoint;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public boolean isAvailable() {
    return available;
  }

  public void setAvailable(boolean available) {
    this.available = available;
  }

  public boolean isArchived() {
    return archived;
  }

  public void setArchived(boolean archived) {
    this.archived = archived;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getPlacedBy() {
    return placedBy;
  }

  public void setPlacedBy(String placedBy) {
    this.placedBy = placedBy;
  }

  @OneToOne(fetch=FetchType.LAZY)
  public User getOwner() {
    return owner;
  }

  public void setOwner(User owner) {
    this.owner = owner;
  }

  @Enumerated(EnumType.STRING)
  public Type getType() {
    return type;
  }

  public void setType(Type type) {
    this.type = type;
  }

  public String getContainer() {
    return container;
  }

  public void setContainer(String container) {
    this.container = container;
  }

  @ManyToMany(fetch=FetchType.LAZY)
  public List<Attribute> getAttributes() {
    return attributes;
  }

  public void setAttributes(List<Attribute> attributes) {
    this.attributes = attributes;
  }

  public String getDifficulty() {
    return difficulty;
  }

  public void setDifficulty(String difficulty) {
    this.difficulty = difficulty;
  }

  public String getTerrain() {
    return terrain;
  }

  public void setTerrain(String terrain) {
    this.terrain = terrain;
  }

  public String getCountry() {
    return country;
  }

  public void setCountry(String country) {
    this.country = country;
  }

  public String getState() {
    return state;
  }

  public void setState(String state) {
    this.state = state;
  }

  public String getShortDescription() {
    return shortDescription;
  }

  public void setShortDescription(String shortDescription) {
    this.shortDescription = shortDescription;
  }

  public boolean isShortDescriptionHtml() {
    return shortDescriptionHtml;
  }

  public void setShortDescriptionHtml(boolean shortDescriptionHtml) {
    this.shortDescriptionHtml = shortDescriptionHtml;
  }

  public String getLongDescription() {
    return longDescription;
  }

  public void setLongDescription(String longDescription) {
    this.longDescription = longDescription;
  }

  public boolean isLongDescriptionHtml() {
    return longDescriptionHtml;
  }

  public void setLongDescriptionHtml(boolean longDescriptionHtml) {
    this.longDescriptionHtml = longDescriptionHtml;
  }

  public String getEncodedHints() {
    return encodedHints;
  }

  public void setEncodedHints(String encodedHints) {
    this.encodedHints = encodedHints;
  }

  @OneToMany(fetch=FetchType.LAZY)
  public List<Log> getLogs() {
    return logs;
  }

  public void setLogs(List<Log> logs) {
    this.logs = logs;
  }

  @OneToMany(fetch=FetchType.LAZY)
  public List<TravelBug> getTravelBugs() {
    return travelBugs;
  }

  public void setTravelBugs(List<TravelBug> travelBugs) {
    this.travelBugs = travelBugs;
  }

  @Override
  public String toString() {
    return "Cache{"
        + "name='" + name + '\''
        + '}';
  }
}
