package com.Nteam.backend.formediapi.hospital.repository;

import com.Nteam.backend.formediapi.hospital.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, String> {
    @Query("SELECT r FROM Review r WHERE r.hospital.hospital_key = :hospitalId")
    List<Review> findByHospitalId(@Param("hospitalId") String hospitalId);
}
