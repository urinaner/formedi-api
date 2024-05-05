package com.Nteam.backend.formediapi.hospital.entity;

import lombok.Getter;
import lombok.Setter;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity(name = "Hospital")
@Getter
@Setter
public class Hospital {
    @Id
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

    @ManyToMany
    @JoinTable(
            name = "hospital_nation",
            joinColumns = @JoinColumn(name = "hospital_key"),
            inverseJoinColumns = @JoinColumn(name = "nation_id")
    )
    private Set<Nation> hospital_nations = new HashSet<>();
}
