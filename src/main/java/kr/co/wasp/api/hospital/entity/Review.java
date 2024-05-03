package kr.co.wasp.api.hospital.entity;
import jakarta.persistence.Entity;

import lombok.Getter;
import lombok.Setter;

import jakarta.persistence.*;

@Entity(name = "Review")
@Getter
@Setter
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String comment;

    private int rating;

    private String reviewer; // 리뷰 작성자

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hospital_id", nullable = false)
    private Hospital hospital;
}

