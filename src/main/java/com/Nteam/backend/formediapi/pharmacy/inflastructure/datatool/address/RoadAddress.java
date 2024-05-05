package com.Nteam.backend.formediapi.pharmacy.inflastructure.datatool.address;

import com.fasterxml.jackson.annotation.JsonProperty;

public class RoadAddress {
    @JsonProperty("address_name")
    private String addressName;
    @JsonProperty("region_1depth_name")
    private String region1depthName;
    @JsonProperty("region_2depth_name")
    private String region2depthName;
    @JsonProperty("region_3depth_name")
    private String region3depthName;
    @JsonProperty("road_name")
    private String roadName;
    @JsonProperty("underground_yn")
    private String undergroundYn;
    @JsonProperty("main_building_no")
    private String mainBuildingNo;
    @JsonProperty("sub_building_no")
    private String subBuildingNo;
    @JsonProperty("building_name")
    private String buildingName;
    @JsonProperty("zone_no")
    private String zoneNo;
    @JsonProperty("y")
    private double y;
    @JsonProperty("x")
    private double x;

    // Getters and Setters
}
