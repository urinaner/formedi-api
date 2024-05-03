package kr.co.wasp.api.pharmacy.inflastructure.datatool.address;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
@Data
public class Document {
    @JsonProperty("address")
    private Address address;
    @JsonProperty("address_name")
    private String addressName;
    @JsonProperty("address_type")
    private String addressType;
    @JsonProperty("road_address")
    private RoadAddress roadAddress;
    @JsonProperty("x")
    private double x;
    @JsonProperty("y")
    private double y;

    // Getters and Setters
}
