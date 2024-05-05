package com.Nteam.backend.formediapi.hospital.dto;

import lombok.*;
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReviewDto {
    private Long id;
    private String comment;
    private int rating; // 별점
    private String hospitalId;
    private String reviewer;
}

