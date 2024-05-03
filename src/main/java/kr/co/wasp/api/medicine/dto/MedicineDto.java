package kr.co.wasp.api.medicine.dto;

import kr.co.wasp.api.medicine.entity.Medicine;
import kr.co.wasp.api.medicine.entity.MedicineCategory;
import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MedicineDto {
    private Long medicine_id;

    private String medicine_name;
    private String medicine_name_en;
    private String medicine_name_ch;
    private String medicine_name_ja;


    private String medicine_image;

    private MedicineCategory medicineCategory;

    private String medicine_efficacy;

    private String instructions;

    private String medicine_caution;

    private String medicine_side_effect;

    public static MedicineDto medicineDto(Medicine medicine){
        MedicineDto medicineDto = new MedicineDto();
        medicineDto.setMedicine_id(medicine.getMedicine_id());
        medicineDto.setMedicine_name(medicine.getMedicine_name());
        medicineDto.setMedicine_image(medicine.getMedicine_image());
        medicineDto.setMedicineCategory(medicine.getMedicineCategory());
        medicineDto.setMedicine_efficacy(medicine.getMedicine_efficacy());
        medicineDto.setInstructions(medicine.getInstructions());
        medicineDto.setMedicine_caution(medicine.getMedicine_caution());
        medicineDto.setMedicine_side_effect(medicine.getMedicine_side_effect());

        return medicineDto;
    }
}

