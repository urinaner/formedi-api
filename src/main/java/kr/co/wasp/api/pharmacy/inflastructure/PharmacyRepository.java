package kr.co.wasp.api.pharmacy.inflastructure;
import kr.co.wasp.api.pharmacy.domain.PharmacyEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PharmacyRepository extends JpaRepository<PharmacyEntity, Long> {

    List<PharmacyEntity> findByEnglishTrue();
    List<PharmacyEntity> findByChineseTrue();
    List<PharmacyEntity> findByJapaneseTrue();
    // Finds pharmacies by name containing a specified string

    @Query("SELECT p FROM PharmacyEntity p WHERE LOWER(p.name) LIKE %:district%")
    List<PharmacyEntity> findByNameContaining(@Param("district") String district);
    @Query("SELECT h FROM PharmacyEntity h WHERE h.district = :gu")
    List<PharmacyEntity> findByDistict(@Param("gu") String gu);



}
