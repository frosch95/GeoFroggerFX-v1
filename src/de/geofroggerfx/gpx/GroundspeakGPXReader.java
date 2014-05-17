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
package de.geofroggerfx.gpx;

import de.geofroggerfx.application.ProgressEvent;
import de.geofroggerfx.application.ProgressListener;
import de.geofroggerfx.model.Attribute;
import de.geofroggerfx.model.*;
import org.jdom2.*;
import org.jdom2.input.SAXBuilder;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * TODO: describe the class
 *
 * @author Andreas Billmann
 */
public class GroundspeakGPXReader implements GPXReader {

  public static final String LAT = "lat";
  public static final String LON = "lon";
  public static final String TIME = "time";
  public static final String NAME = "name";
  public static final String DESC = "desc";
  public static final String WPT = "wpt";
  public static final String URL = "url";
  public static final String URLNAME = "urlname";
  public static final String SYM = "sym";
  public static final String TYPE = "type";
  public static final String CACHE = "cache";
  public static final String GROUNDSPEAK = "groundspeak";
  public static final String ID = "id";
  public static final String AVAILABLE = "available";
  public static final String ARCHIVED = "archived";
  public static final String PLACED_BY = "placed_by";
  public static final String OWNER = "owner";
  public static final String CONTAINER = "container";
  public static final String ATTRIBUTES = "attributes";
  public static final String ATTRIBUTE = "attribute";
  public static final String INC = "inc";
  public static final String DIFFICULTY = "difficulty";
  public static final String TERRAIN = "terrain";
  public static final String COUNTRY = "country";
  public static final String STATE = "state";
  public static final String SHORT_DESCRIPTION = "short_description";
  public static final String HTML = "html";
  public static final String LONG_DESCRIPTION = "long_description";
  public static final String ENCODED_HINTS = "encoded_hints";
  public static final String LOGS = "logs";
  public static final String LOG = "log";
  public static final String DATE = "date";
  public static final String FINDER = "finder";
  public static final String ENCODED = "encoded";
  public static final String TRAVELBUGS = "travelbugs";
  public static final String TRAVELBUG = "travelbug";
  public static final String TEXT = "text";
  public static final String REF = "ref";
  public static final String DEFAULT_NAMESPACE_URL = "http://www.topografix.com/GPX/1/0";
  public static final String GROUNDSPEAK_NAMESPACE_URL = "http://www.groundspeak.com/cache/1/0/1";

  private final List<ProgressListener> listeners = new ArrayList<>();

  private final Map<Long, User> userCache = new HashMap<>();

  private File gpxFile;
  private Document content;
  private List<Cache> modelList;
  private Namespace defaultNamespace;
  private Namespace groundspeakNamespace;

  @Override
  public void addListener(ProgressListener listener) {
    if (!listeners.contains(listener)) {
      listeners.add(listener);
    }
  }

  @Override
  public List<Cache> load(final String filename) throws IOException {

    checkIfParameterIsNull(filename);
    instantiateFileAndAssignToMember(filename);
    checkIfFileExists();
    readFileContent();
    setNamespaces();
    parseContent();

    return modelList;
  }

  private void setNamespaces() {
    defaultNamespace = Namespace.getNamespace(DEFAULT_NAMESPACE_URL);
    groundspeakNamespace = Namespace.getNamespace(GROUNDSPEAK, GROUNDSPEAK_NAMESPACE_URL);
  }

  private void checkIfParameterIsNull(String filename) {
    if (filename == null) {
      throw new IllegalArgumentException("filename should not be null");
    }
  }

  private void instantiateFileAndAssignToMember(String filename) {
    gpxFile = new File(filename);
  }

  private void checkIfFileExists() throws FileNotFoundException {
    if (!gpxFile.exists()) {
      throw new FileNotFoundException("file does not exist");
    }
  }

  private void readFileContent() throws IOException {
    fireEvent(new ProgressEvent("GPX Reader",
        ProgressEvent.State.STARTED,
        "Load File " + gpxFile.getName() + " started."));
    countLineNumbers();

    final SAXBuilder saxBuilder = new SAXBuilder();
    try {
      content = saxBuilder.build(gpxFile);
    } catch (JDOMException e) {
      throw new IOException(e.getMessage(), e);
    }
    fireEvent(new ProgressEvent("GPX Reader",
        ProgressEvent.State.FINISHED,
        "Load File " + gpxFile.getName() + " finished."));
  }

  private void parseContent() {
    modelList = new ArrayList<>();
    try {
      final Element root = content.getRootElement();
      final List<Element> waypoints = root.getChildren(WPT, defaultNamespace);

      int totalNumberOfCaches = waypoints.size();
      int currentNumber = 0;
      for (final Element waypointElement : waypoints) {
        currentNumber++;
        fireEvent(new ProgressEvent("GPX Reader",
            ProgressEvent.State.RUNNING,
            "Parse " + currentNumber + " of " + totalNumberOfCaches + " caches.",
            (double) currentNumber / (double) totalNumberOfCaches));
        final Cache cache = parseWaypointElement(waypointElement);
        cache.setFound(CacheUtils.hasUserFoundCache(cache, 3906456l));
        modelList.add(cache);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private Cache parseWaypointElement(Element waypointElement) {
    final Cache cache = new Cache();
    final Waypoint mainWaypoint = new Waypoint();
    cache.setMainWayPoint(mainWaypoint);

    try {
      setLatitudeAndLongitude(waypointElement, mainWaypoint);
      setTime(waypointElement, mainWaypoint);
      setWaypointName(waypointElement, mainWaypoint);
      setDescription(waypointElement, mainWaypoint);
      setUrl(waypointElement, mainWaypoint);
      setUrlName(waypointElement, mainWaypoint);
      setSym(waypointElement, mainWaypoint);
      setType(waypointElement, mainWaypoint);

      final Element cacheElement = waypointElement.getChild(CACHE, groundspeakNamespace);
      setId(cacheElement, mainWaypoint);
      parseCacheElement(cacheElement, cache);
    } catch (DataConversionException | MalformedURLException e) {
      // TODO: do some batch error handling
      e.printStackTrace();
    }
    return cache;
  }

  private void parseCacheElement(Element cacheElement, Cache cache) throws DataConversionException {
    setId(cacheElement, cache);
    setAvailable(cacheElement, cache);
    setArchived(cacheElement, cache);
    setName(cacheElement, cache);
    setPlacedBy(cacheElement, cache);
    setOwner(cacheElement, cache);
    setType(cacheElement, cache);
    setContainer(cacheElement, cache);
    setAttributes(cacheElement, cache);
    setDifficulty(cacheElement, cache);
    setTerrain(cacheElement, cache);
    setCountry(cacheElement, cache);
    setState(cacheElement, cache);
    setShortDescription(cacheElement, cache);
    setLongDescription(cacheElement, cache);
    setEncodedHints(cacheElement, cache);
    setLogs(cacheElement, cache);
    setTravelBugs(cacheElement, cache);
  }

  private void setTravelBugs(Element cacheElement, Cache cache) throws DataConversionException {
    final Element travelBugsElement = cacheElement.getChild(TRAVELBUGS, groundspeakNamespace);
    if (travelBugsElement != null) {
      final List<TravelBug> travelBugs = new ArrayList<>();
      cache.setTravelBugs(travelBugs);
      for (Element travelBugElement : travelBugsElement.getChildren()) {
        final TravelBug travelBug = new TravelBug();
        travelBugs.add(travelBug);
        setId(travelBugElement, travelBug);
        setRef(travelBugElement, travelBug);
        setName(travelBugElement, travelBug);
      }
    }
  }

  private void setName(Element travelBugElement, TravelBug travelBug) {
    travelBug.setName(travelBugElement.getChild(NAME, groundspeakNamespace).getTextTrim());
  }

  private void setRef(Element travelBugElement, TravelBug travelBug) {
    travelBug.setRef(travelBugElement.getAttribute(REF).getValue());
  }

  private void setId(Element travelBugElement, TravelBug travelBug) throws DataConversionException {
    travelBug.setId(travelBugElement.getAttribute(ID).getLongValue());
  }

  private void setLogs(Element cacheElement, Cache cache) throws DataConversionException {
    final Element logsElement = cacheElement.getChild(LOGS, groundspeakNamespace);
    if (logsElement != null) {
      final List<Log> logs = new ArrayList<>();
      cache.setLogs(logs);
      for (Element logElement : logsElement.getChildren()) {
        final Log log = new Log();
        logs.add(log);
        setId(logElement, log);
        setDate(logElement, log);
        setType(logElement, log);
        setText(logElement, log);
        setFinder(logElement, log);
      }
    }
  }

  private void setFinder(Element logElement, Log log) throws DataConversionException {
    final Element finderElement = logElement.getChild(FINDER, groundspeakNamespace);
    if (finderElement != null) {
      final Long finderId = finderElement.getAttribute(ID).getLongValue();

      User finder = userCache.get(finderId);
      if (finder == null) {
        finder = new User();
        setId(finderElement, finder);
        setName(finderElement, finder);
      }
      log.setFinder(finder);
    }
  }

  private void setText(Element logElement, Log log) throws DataConversionException {
    final Element textElement = logElement.getChild(TEXT, groundspeakNamespace);
    log.setText(textElement.getTextTrim());
    log.setTextEncoded(textElement.getAttribute(ENCODED).getBooleanValue());
  }

  private void setType(Element logElement, Log log) {
    log.setType(logElement.getChild(TYPE, groundspeakNamespace).getTextTrim());
  }

  private void setId(Element logElement, Log log) throws DataConversionException {
    log.setId(logElement.getAttribute(ID).getLongValue());
  }

  private void setDate(Element logElement, Log log) {
    final String dateText = logElement.getChild(DATE, groundspeakNamespace).getTextTrim();
    final LocalDateTime date = LocalDateTime.parse(dateText, DateTimeFormatter.ISO_DATE_TIME);
    log.setDate(date);
  }

  private void setEncodedHints(Element cacheElement, Cache cache) {
    cache.setEncodedHints(cacheElement.getChild(ENCODED_HINTS, groundspeakNamespace).getTextTrim());
  }

  private void setLongDescription(Element cacheElement, Cache cache) throws DataConversionException {
    final Element longDescription = cacheElement.getChild(LONG_DESCRIPTION, groundspeakNamespace);
    cache.setLongDescription(longDescription.getTextTrim());
    cache.setLongDescriptionHtml(longDescription.getAttribute(HTML).getBooleanValue());
  }

  private void setShortDescription(Element cacheElement, Cache cache) throws DataConversionException {
    final Element shortDescription = cacheElement.getChild(SHORT_DESCRIPTION, groundspeakNamespace);
    cache.setShortDescription(shortDescription.getTextTrim());
    cache.setShortDescriptionHtml(shortDescription.getAttribute(HTML).getBooleanValue());
  }

  private void setState(Element cacheElement, Cache cache) {
    cache.setState(cacheElement.getChild(STATE, groundspeakNamespace).getTextTrim());
  }

  private void setCountry(Element cacheElement, Cache cache) {
    cache.setCountry(cacheElement.getChild(COUNTRY, groundspeakNamespace).getTextTrim());
  }

  private void setTerrain(Element cacheElement, Cache cache) {
    cache.setTerrain(cacheElement.getChild(TERRAIN, groundspeakNamespace).getTextTrim());
  }

  private void setDifficulty(Element cacheElement, Cache cache) {
    cache.setDifficulty(cacheElement.getChild(DIFFICULTY, groundspeakNamespace).getTextTrim());
  }

  private void setAttributes(Element cacheElement, Cache cache) throws DataConversionException {
    final Element attributesElement = cacheElement.getChild(ATTRIBUTES, groundspeakNamespace);
    if (attributesElement != null) {
      final List attributes = new ArrayList<>();
      cache.setAttributes(attributes);
      for (Element attributeElement : attributesElement.getChildren()) {
        final Attribute attribute = new Attribute();
        attributes.add(attribute);
        setId(attributeElement, attribute);
        setInc(attributeElement, attribute);
        setText(attributeElement, attribute);
      }
    }
  }

  private void setText(Element attributeElement, Attribute attribute) {
    attribute.setText(attributeElement.getTextTrim());
  }

  private void setInc(Element attributeElement, Attribute attribute) throws DataConversionException {
    attribute.setInc(attributeElement.getAttribute(INC).getBooleanValue());
  }

  private void setId(Element attributeElement, Attribute attribute) throws DataConversionException {
    attribute.setId(attributeElement.getAttribute(ID).getLongValue());
  }

  private void setContainer(Element cacheElement, Cache cache) {
    cache.setContainer(cacheElement.getChild(CONTAINER, groundspeakNamespace).getTextTrim());
  }

  private void setType(Element cacheElement, Cache cache) {
    cache.setType(Type.groundspeakStringToType(cacheElement.getChild(TYPE, groundspeakNamespace).getTextTrim()));
  }

  private void setOwner(Element cacheElement, Cache cache) throws DataConversionException {
    final Element ownerElement = cacheElement.getChild(OWNER, groundspeakNamespace);
    if (ownerElement != null) {
      final Long userId = ownerElement.getAttribute(ID).getLongValue();
      User owner = userCache.get(userId);
      if (owner == null) {
        owner = new User();
        setId(ownerElement, owner);
        setName(ownerElement, owner);
      }
      cache.setOwner(owner);
    }
  }

  private void setId(Element userElement, User user) throws DataConversionException {
    user.setId(userElement.getAttribute(ID).getLongValue());
  }

  private void setName(Element userElement, User user) {
    user.setName(userElement.getTextTrim());
  }

  private void setPlacedBy(Element cacheElement, Cache cache) {
    cache.setPlacedBy(cacheElement.getChild(PLACED_BY, groundspeakNamespace).getTextTrim());
  }

  private void setArchived(Element cacheElement, Cache cache) throws DataConversionException {
    cache.setArchived(cacheElement.getAttribute(ARCHIVED).getBooleanValue());
  }

  private void setAvailable(Element cacheElement, Cache cache) throws DataConversionException {
    cache.setAvailable(cacheElement.getAttribute(AVAILABLE).getBooleanValue());
  }

  private void setId(Element cacheElement, Cache cache) throws DataConversionException {
    cache.setId(cacheElement.getAttribute(ID).getLongValue());
  }

  private void setType(Element cacheElement, Waypoint waypoint) {
    waypoint.setType(cacheElement.getChild(TYPE, defaultNamespace).getTextTrim());
  }

  private void setSym(Element cacheElement, Waypoint waypoint) {
    waypoint.setSymbol(cacheElement.getChild(SYM, defaultNamespace).getTextTrim());
  }

  private void setUrlName(Element cacheElement, Waypoint waypoint) throws MalformedURLException {
    waypoint.setUrlName(cacheElement.getChild(URLNAME, defaultNamespace).getTextTrim());
  }

  private void setUrl(Element cacheElement, Waypoint waypoint) throws MalformedURLException {
    waypoint.setUrl(new URL(cacheElement.getChild(URL, defaultNamespace).getTextTrim()));
  }

  private void setDescription(Element cacheElement, Waypoint waypoint) {
    waypoint.setDescription(cacheElement.getChild(DESC, defaultNamespace).getTextTrim());
  }

  private void setWaypointName(Element cacheElement, Waypoint waypoint) {
    waypoint.setName(cacheElement.getChild(NAME, defaultNamespace).getTextTrim());
  }

  private void setLatitudeAndLongitude(Element cacheElement, Waypoint waypoint) throws DataConversionException {
    waypoint.setLatitude(cacheElement.getAttribute(LAT).getDoubleValue());
    waypoint.setLongitude(cacheElement.getAttribute(LON).getDoubleValue());
  }

  private void setTime(Element cacheElement, Waypoint waypoint) {
    final String timeText = cacheElement.getChild(TIME, defaultNamespace).getTextTrim();
    final LocalDateTime date = LocalDateTime.parse(timeText, DateTimeFormatter.ISO_DATE_TIME);
    waypoint.setTime(date);
  }

  private void setName(Element cacheElement, Cache cache) {
    cache.setName(cacheElement.getChild(NAME, groundspeakNamespace).getTextTrim());
  }

  private void setId(Element cacheElement, Waypoint waypoint) throws DataConversionException {
    waypoint.setId(cacheElement.getAttribute(ID).getLongValue());
  }

  private void fireEvent(ProgressEvent event) {
    listeners.stream().forEach((l) -> {
      l.progress(event);
    });
  }

  private void countLineNumbers() {
    int totalNumberOfLines = 0;
    Charset charset = Charset.forName("UTF-8");
    StringBuilder sb = new StringBuilder();
    try (BufferedReader reader = Files.newBufferedReader(gpxFile.toPath(), charset)) {
      String line = null;
      while ((line = reader.readLine()) != null) {
        totalNumberOfLines++;
      }
      fireEvent(new ProgressEvent("GPX Reader",
          ProgressEvent.State.RUNNING,
          totalNumberOfLines + " lines to read!"));
    } catch (IOException x) {
      x.printStackTrace();
    }
  }
}
