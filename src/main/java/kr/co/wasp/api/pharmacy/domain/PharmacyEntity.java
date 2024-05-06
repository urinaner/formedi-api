package kr.co.wasp.api.pharmacy.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "pharmacy_table")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PharmacyEntity {
    @Id
    private Long phar_id;
    private String district;
    private String name;
    private String phar_name_en;
    private String phar_name_ch;
    private String phar_name_ja;
    private String address;
    private double latitude;
    private double longitude;
    private String phone;
    private boolean english;
    private boolean chinese;
    private boolean japanese;
}
