package com.Nteam.backend.formediapi.pharmacy.inflastructure.datatool.address;


import lombok.Data;

import java.util.List;

@Data
public class RootDto {
    private List<Document> documents;
    private Meta meta;

    // Getters and Setters
    // documents 리스트가 비어 있는지 확인하는 메소드
    public boolean isDocumentsEmpty() {
        return documents.isEmpty();
    }
}