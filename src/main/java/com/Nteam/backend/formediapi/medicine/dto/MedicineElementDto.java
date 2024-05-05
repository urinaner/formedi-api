package com.Nteam.backend.formediapi.medicine.dto;

import com.Nteam.backend.formediapi.medicine.entity.Medicine;
import com.Nteam.backend.formediapi.medicine.entity.MedicineCategory;
import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MedicineElementDto {
    private Long medicine_id;

    private String medicine_name;

    private String medicine_image;

    private MedicineCategory medicineCategory;

    public MedicineElementDto(Medicine medicine) {
        this.medicine_id = medicine.getMedicine_id();
        this.medicine_name = medicine.getMedicine_name();
        this.medicine_image = medicine.getMedicine_image();
        this.medicineCategory = medicine.getMedicineCategory();
    }
}

