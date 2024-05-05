package com.Nteam.backend.formediapi.hospital.repository;

import com.Nteam.backend.formediapi.hospital.entity.Nation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NationRepository extends JpaRepository<Nation, Long> {
    Nation findByName(String name);
}
