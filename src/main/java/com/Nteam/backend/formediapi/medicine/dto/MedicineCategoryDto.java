package com.Nteam.backend.formediapi.medicine.dto;

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

