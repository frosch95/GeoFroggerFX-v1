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
package de.geofroggerfx.model;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Andreas Billmann
 */
public enum Attribute {

  // id:1 text:Dogs
  DOGS_TRUE(1),
  DOGS_FALSE(-1),

  // id:2 text:Access or parking fee
  ACCESS_OR_PARKING_FEE_TRUE(2),
  ACCESS_OR_PARKING_FEE_FALSE(-2),

  // id:3 text:Climbing gear
  CLIMBING_GEAR_TRUE(3),
  CLIMBING_GEAR_FALSE(-3),

  // id:4 text:Boat
  BOAT_TRUE(4),
  BOAT_FALSE(-4),

  // id:5 text:Scuba gear
  SCUBA_GEAR_TRUE(5),
  SCUBA_GEAR_FALSE(-5),

  // id:6 text:Recommended for kids
  RECOMMENDED_FOR_KIDS_TRUE(6),
  RECOMMENDED_FOR_KIDS_FALSE(-6),

  // id:7 text:Takes less than an hour
  TAKES_LESS_THAN_AN_HOUR_TRUE(7),
  TAKES_LESS_THAN_AN_HOUR_FALSE(-7),

  // id:8 text:Scenic view
  SCENIC_VIEW_TRUE(8),
  SCENIC_VIEW_FALSE(-8),

  // id:9 text:Significant Hike
  SIGNIFICANT_HIKE_TRUE(9),
  SIGNIFICANT_HIKE_FALSE(-9),

  // id:10 text:Difficult climbing
  DIFFICULT_CLIMBING_TRUE(10),
  DIFFICULT_CLIMBING_FALSE(-10),

  // id:11 text:May require wading
  MAY_REQUIRE_WADING_TRUE(11),
  MAY_REQUIRE_WADING_FALSE(-11),

  // id:12 text:May require swimming
  MAY_REQUIRE_SWIMMING_TRUE(12),
  MAY_REQUIRE_SWIMMING_FALSE(-12),

  // id:13 text:Available at all times
  AVAILABLE_AT_ALL_TIMES_TRUE(13),
  AVAILABLE_AT_ALL_TIMES_FALSE(-13),

  // id:14 text:Recommended at night
  RECOMMENDED_AT_NIGHT_TRUE(14),
  RECOMMENDED_AT_NIGHT_FALSE(-14),

  // id:15 text:Available during winter
  AVAILABLE_DURING_WINTER_TRUE(15),
  AVAILABLE_DURING_WINTER_FALSE(-15),

  // id:17 text:Poison plants
  POISON_PLANTS_TRUE(17),
  POISON_PLANTS_FALSE(-17),

  // id:18 text:Dangerous Animals
  DANGEROUS_ANIMALS_TRUE(18),
  DANGEROUS_ANIMALS_FALSE(-18),

  // id:19 text:Ticks
  TICKS_TRUE(19),
  TICKS_FALSE(-19),

  // id:20 text:Abandoned mines
  ABANDONED_MINES_TRUE(20),
  ABANDONED_MINES_FALSE(-20),

  // id:21 text:Cliff / falling rocks
  CLIFF_FALLING_ROCKS_TRUE(21),
  CLIFF_FALLING_ROCKS_FALSE(-21),

  // id:22 text:Hunting
  HUNTING_TRUE(22),
  HUNTING_FALSE(-22),

  // id:23 text:Dangerous area
  DANGEROUS_AREA_TRUE(23),
  DANGEROUS_AREA_FALSE(-23),

  // id:24 text:Wheelchair accessible
  WHEELCHAIR_ACCESSIBLE_TRUE(24),
  WHEELCHAIR_ACCESSIBLE_FALSE(-24),

  // id:25 text:Parking available
  PARKING_AVAILABLE_TRUE(25),
  PARKING_AVAILABLE_FALSE(-25),

  // id:26 text:Public transportation
  PUBLIC_TRANSPORTATION_TRUE(26),
  PUBLIC_TRANSPORTATION_FALSE(-26),

  // id:27 text:Drinking water nearby
  DRINKING_WATER_NEARBY_TRUE(27),
  DRINKING_WATER_NEARBY_FALSE(-27),

  // id:28 text:Public restrooms nearby
  PUBLIC_RESTROOMS_NEARBY_TRUE(28),
  PUBLIC_RESTROOMS_NEARBY_FALSE(-28),

  // id:29 text:Telephone nearby
  TELEPHONE_NEARBY_TRUE(29),
  TELEPHONE_NEARBY_FALSE(-29),

  // id:30 text:Picnic tables nearby
  PICNIC_TABLES_NEARBY_TRUE(30),
  PICNIC_TABLES_NEARBY_FALSE(-30),

  // id:31 text:Camping available
  CAMPING_AVAILABLE_TRUE(31),
  CAMPING_AVAILABLE_FALSE(-31),

  // id:32 text:Bicycles
  BICYCLES_TRUE(32),
  BICYCLES_FALSE(-32),

  // id:33 text:Motorcycles
  MOTORCYCLES_TRUE(33),
  MOTORCYCLES_FALSE(-33),

  // id:34 text:Quads
  QUADS_TRUE(34),
  QUADS_FALSE(-34),

  // id:35 text:Off-road vehicles
  OFF_ROAD_VEHICLES_TRUE(35),
  OFF_ROAD_VEHICLES_FALSE(-35),

  // id:36 text:Snowmobiles
  SNOWMOBILES_TRUE(36),
  SNOWMOBILES_FALSE(-36),

  // id:37 text:Horses
  HORSES_TRUE(37),
  HORSES_FALSE(-37),

  // id:38 text:Campfires
  CAMPFIRES_TRUE(38),
  CAMPFIRES_FALSE(-38),

  // id:39 text:Thorns
  THORNS_TRUE(39),
  THORNS_FALSE(-39),

  // id:40 text:Stealth required
  STEALTH_REQUIRED_TRUE(40),
  STEALTH_REQUIRED_FALSE(-40),

  // id:41 text:Stroller accessible
  STROLLER_ACCESSIBLE_TRUE(41),
  STROLLER_ACCESSIBLE_FALSE(-41),

  // id:43 text:Watch for livestock
  WATCH_FOR_LIVESTOCK_TRUE(43),
  WATCH_FOR_LIVESTOCK_FALSE(-43),

  // id:42 text:Needs maintenance
  NEEDS_MAINTENANCE_TRUE(42),
  NEEDS_MAINTENANCE_FALSE(-42),

  // id:44 text:Flashlight required
  FLASHLIGHT_REQUIRED_TRUE(44),
  FLASHLIGHT_REQUIRED_FALSE(-44),

  // id:45 text:Lost and Found Tour
  LOST_AND_FOUND_TOUR_TRUE(45),
  LOST_AND_FOUND_TOUR_FALSE(-45),

  // id:46 text:Truck Driver/RV
  TRUCK_DRIVER_RV_TRUE(46),
  TRUCK_DRIVER_RV_FALSE(-46),

  // id:47 text:Field Puzzle
  FIELD_PUZZLE_TRUE(47),
  FIELD_PUZZLE_FALSE(-47),

  // id:48 text:UV Light Required
  UV_LIGHT_REQUIRED_TRUE(48),
  UV_LIGHT_REQUIRED_FALSE(-48),

  // id:49 text:Snowshoes
  SNOWSHOES_TRUE(49),
  SNOWSHOES_FALSE(-49),

  // id:50 text:Cross Country Skis
  CROSS_COUNTRY_SKIS_TRUE(50),
  CROSS_COUNTRY_SKIS_FALSE(-50),

  // id:51 text:Special Tool Required
  SPECIAL_TOOL_REQUIRED_TRUE(51),
  SPECIAL_TOOL_REQUIRED_FALSE(-51),

  // id:52 text:Night Cache
  NIGHT_CACHE_TRUE(52),
  NIGHT_CACHE_FALSE(-52),

  // id:53 text:Park and Grab
  PARK_AND_GRAB_TRUE(53),
  PARK_AND_GRAB_FALSE(-53),

  // id:54 text:Abandoned Structure
  ABANDONED_STRUCTURE_TRUE(54),
  ABANDONED_STRUCTURE_FALSE(-54),

  // id:55 text:Short hike (less than 1km)
  SHORT_HIKE_LESS_THAN_1KM_TRUE(55),
  SHORT_HIKE_LESS_THAN_1KM_FALSE(-55),

  // id:56 text:Medium hike (1km-10km)
  MEDIUM_HIKE_1KM_10KM_TRUE(56),
  MEDIUM_HIKE_1KM_10KM_FALSE(-56),

  // id:57 text:Long Hike (+10km)
  LONG_HIKE_10KM_TRUE(57),
  LONG_HIKE_10KM_FALSE(-57),

  // id:58 text:Fuel Nearby
  FUEL_NEARBY_TRUE(58),
  FUEL_NEARBY_FALSE(-58),

  // id:59 text:Food Nearby
  FOOD_NEARBY_TRUE(59),
  FOOD_NEARBY_FALSE(-59),

  // id:60 text:Wireless Beacon
  WIRELESS_BEACON_TRUE(60),
  WIRELESS_BEACON_FALSE(-60),

  // id:61 text:Partnership cache
  PARTNERSHIP_CACHE_TRUE(61),
  PARTNERSHIP_CACHE_FALSE(-61),

  // id:62 text:Seasonal Access
  SEASONAL_ACCESS_TRUE(62),
  SEASONAL_ACCESS_FALSE(-62),

  // id:63 text:Tourist Friendly
  TOURIST_FRIENDLY_TRUE(63),
  TOURIST_FRIENDLY_FALSE(-63),

  // id:64 text:Tree Climbing
  TREE_CLIMBING_TRUE(64),
  TREE_CLIMBING_FALSE(-64),

  // id:65 text:Front Yard (Private Residence)
  FRONT_YARD_PRIVATE_RESIDENCE_TRUE(65),
  FRONT_YARD_PRIVATE_RESIDENCE_FALSE(-65),

  // id:66 text:Teamwork Required
  TEAMWORK_REQUIRED_TRUE(66),
  TEAMWORK_REQUIRED_FALSE(-66),

  // id:67 text:geotour
  GEOTOUR_TRUE(67),
  GEOTOUR_FALSE(-67);




  private final static List<Long> attributes = new ArrayList<>();

  private int id;

  private Attribute(int id) {
    this.id = id;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public static Attribute groundspeakAttributeToAttribute(Long id, boolean inc, String text) {

    int idToCompare = id.intValue();

    if (!inc) {
      idToCompare = -idToCompare;
    }

    for (Attribute t: Attribute.values()) {
      if (t.getId() == idToCompare) {
        return t;
      }
    }

    if (!attributes.contains(id)) {
      attributes.add(id);
      System.out.println();
      System.out.println("// id:"+id+" text:"+text);
      System.out.println(text.toUpperCase().replace(' ','_') + "_TRUE" + "(" + id + "),");
      System.out.println(text.toUpperCase().replace(' ','_') + "_FALSE" + "(-" + id + "),");
    }

    throw new IllegalArgumentException("unknown attribute id:"+id+" inc:"+inc+" text:"+text);
  }
}
