package com.Nteam.backend.formediapi.medicine.entity;

import com.Nteam.backend.formediapi.medicine.dto.MedicineDto;
import jakarta.persistence.*;
import lombok.*;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Table(name = "medicine_table")
public class Medicine {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long medicine_id;
    private String medicine_name;
    private String medicine_name_en;
    private String medicine_name_ch;
    private String medicine_name_ja;
    private String medicine_image;
    @ManyToOne
    @JoinColumn(name="category_id")
    private MedicineCategory medicineCategory;

    private String medicine_efficacy;

    private String instructions;

    private String medicine_caution;

    private String medicine_side_effect;

    public static Medicine toMedicine(MedicineDto medicineDto){
        Medicine medicine = new Medicine();
        medicine.setMedicine_id(medicineDto.getMedicine_id());
        medicine.setMedicine_name(medicineDto.getMedicine_name());
        medicine.setMedicine_image(medicineDto.getMedicine_image());
        medicine.setMedicineCategory(medicineDto.getMedicineCategory());
        medicine.setInstructions(medicineDto.getInstructions());
        medicine.setMedicine_caution(medicineDto.getMedicine_caution());
        medicine.setMedicine_efficacy(medicineDto.getMedicine_efficacy());
        return medicine;
    }
}