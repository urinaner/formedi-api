package com.Nteam.backend.formediapi.pharmacy.inflastructure.datatool.address;

import com.fasterxml.jackson.annotation.JsonProperty;
public class Address {
    @JsonProperty("address_name")
    private String addressName;
    @JsonProperty("region_1depth_name")
    private String region1depthName;
    @JsonProperty("region_2depth_name")
    private String region2depthName;
    @JsonProperty("region_3depth_name")
    private String region3depthName;
    @JsonProperty("region_3depth_h_name")
    private String region3depthHName;
    @JsonProperty("h_code")
    private String hCode;
    @JsonProperty("b_code")
    private String bCode;
    @JsonProperty("mountain_yn")
    private String mountainYn;
    @JsonProperty("main_address_no")
    private String mainAddressNo;
    @JsonProperty("sub_address_no")
    private String subAddressNo;
    @JsonProperty("x")
    private double x;
    @JsonProperty("y")
    private double y;

    // Getters and Setters
}

