package com.Nteam.backend.formediapi.pharmacy.domain;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class ReviewEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pharmacy_id")
    private PharmacyEntity pharmacy;

    private String reviewer;
    private String content;
}
