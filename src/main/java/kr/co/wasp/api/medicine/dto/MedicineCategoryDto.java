package kr.co.wasp.api.medicine.dto;

import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MedicineCategoryDto {
    private Long category_id;

    private String category_name;
}

