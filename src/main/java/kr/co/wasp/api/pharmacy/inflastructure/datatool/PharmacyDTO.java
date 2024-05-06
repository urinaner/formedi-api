package kr.co.wasp.api.pharmacy.inflastructure.datatool;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class PharmacyDTO {
    private Long id;
    private String district;
    private String name;
    private String name_en;
    private String name_ch;
    private String name_ja;
    private String address;
    private String phone;
    private double longitude;
    private double latitude;
    private boolean english;
    private boolean chinese;
    private boolean japanese;

}

