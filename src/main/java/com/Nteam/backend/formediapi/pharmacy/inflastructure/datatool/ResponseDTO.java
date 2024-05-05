package com.Nteam.backend.formediapi.pharmacy.inflastructure.datatool;

import lombok.Data;

@Data
public class ResponseDTO {
    private double latitude;  // 위도
    private double longitude; // 경도

    public ResponseDTO(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }
}
