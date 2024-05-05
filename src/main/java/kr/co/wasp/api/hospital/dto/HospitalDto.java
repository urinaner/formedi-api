package kr.co.wasp.api.hospital.dto;

import lombok.*;
import java.util.Set;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class HospitalDto {

    private String hospital_key;

    private String hospital_register_num;

    private String hospital_name;
    private String hospital_name_en;
    private String hospital_name_ch;
    private String hospital_name_ja;

    private String hospital_category;

    private String hospital_si;

    private String hospital_gu;

    private String hospital_dong;

    private String hospital_ceo;

    private String hospital_address;

    private Double hospital_latitude;
    private Double hospital_longitude;

    private Set<String> nations;

}
