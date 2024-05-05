package com.Nteam.backend.formediapi.pharmacy.domain;

import jakarta.persistence.*;

@Entity
public class BookmarkEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY) // PharmacyBookmark와 PharmacyEntity는 다대일 관계
    @JoinColumn(name = "pharmacy_id")  // 외래 키는 pharmacy_id로 설정
    private PharmacyEntity pharmacy;

    private Long userId; // 사용자 ID를 가정

}
