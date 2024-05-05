package com.Nteam.backend.formediapi.hospital.repository;

import com.Nteam.backend.formediapi.hospital.entity.Hospital;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HospitalRepository extends JpaRepository<Hospital, String> {
    @Query("SELECT h FROM Hospital h WHERE h.hospital_gu = :gu")
    List<Hospital> findByHospital_gu(@Param("gu") String gu);

    @Query("SELECT h FROM Hospital h WHERE h.hospital_key = :key")
    Hospital findHospitalByHospital_register_num(@Param("key") String key);

    @Query("SELECT h FROM Hospital h WHERE h.hospital_category = :category")
    List<Hospital> findByHospital_category(@Param("category") String category);

    @Query("SELECT h FROM Hospital h WHERE LOWER(h.hospital_name) LIKE %:keyword%")
    List<Hospital> findHospitalsByKeyword(@Param("keyword") String keyword);

    // Nation 별로 해당되는 병원들을 조회
    @Query("SELECT h FROM Hospital h JOIN h.hospital_nations n WHERE n.name = :nationName")
    List<Hospital> findByHospital_nationsContaining(@Param("nationName") String nationName);

}
