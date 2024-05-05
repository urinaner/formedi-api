package com.Nteam.backend.formediapi.pharmacy.inflastructure;
import com.Nteam.backend.formediapi.pharmacy.domain.PharmacyEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PharmacyRepository extends JpaRepository<PharmacyEntity, Long> {

    List<PharmacyEntity> findByEnglishTrue();
    List<PharmacyEntity> findByChineseTrue();
    List<PharmacyEntity> findByJapaneseTrue();
//    List<PharmacyEntity> findByPharNameContaining(String phar_name);
//
//    List<PharmacyEntity> findByPharGu(String phar_gu);

}
