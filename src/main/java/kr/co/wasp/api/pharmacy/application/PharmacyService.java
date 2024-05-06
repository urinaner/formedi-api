package kr.co.wasp.api.pharmacy.application;
import kr.co.wasp.api.pharmacy.domain.PharmacyEntity;
import kr.co.wasp.api.pharmacy.inflastructure.PharmacyRepository;
import kr.co.wasp.api.pharmacy.inflastructure.datatool.PharmacyDTO;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
public class PharmacyService {
    private final PharmacyRepository pharmacyRepository;

    public PharmacyService(PharmacyRepository pharmacyRepository){ //의존성주입
        this.pharmacyRepository = pharmacyRepository;
    }

    public List<PharmacyDTO> getPharmacyList() { //전체조회
        return pharmacyRepository.findAll().stream().map(this::transform).collect(Collectors.toList());
    }

    public PharmacyDTO getPharmacy(Long id) { //하나 조회
        Optional<PharmacyEntity> optionalPharmacy = pharmacyRepository.findById(id);
        System.out.print(id);
        if(optionalPharmacy.isPresent()){
            PharmacyEntity pharmacyEntity = optionalPharmacy.get();
            return PharmacyDTO.builder()
                    .id(pharmacyEntity.getPhar_id())
                    .district(pharmacyEntity.getDistrict())
                    .name(pharmacyEntity.getName())
                    .name_en(pharmacyEntity.getPhar_name_en())
                    .name_ch(pharmacyEntity.getPhar_name_ch())
                    .name_ja(pharmacyEntity.getPhar_name_ja())
                    .address(pharmacyEntity.getAddress())
                    .phone(pharmacyEntity.getPhone())
                    .english(pharmacyEntity.isEnglish())
                    .chinese(pharmacyEntity.isChinese())
                    .japanese(pharmacyEntity.isJapanese())
                    .build();
        }
        return null;
    }
    public List<PharmacyDTO> getPharmaciesByLanguage(int langId) {
        List<PharmacyEntity> pharmacies;
        switch (langId) {
            case 1:
                pharmacies = pharmacyRepository.findByEnglishTrue();
                break;
            case 2:
                pharmacies = pharmacyRepository.findByChineseTrue();
                break;
            case 3:
                pharmacies = pharmacyRepository.findByJapaneseTrue();
                break;
            default:
                pharmacies = new ArrayList<>();
        }
        return pharmacies.stream().map(this::transform).collect(Collectors.toList());
    }

    public List<PharmacyEntity> findNearestPharmacies(double lat, double lon, boolean english, boolean chinese, boolean japanese) {
        List<PharmacyEntity> pharmacies = pharmacyRepository.findAll();
        return pharmacies.stream()
                .filter(p -> p.isEnglish() == english && p.isChinese() == chinese && p.isJapanese() == japanese)
                .sorted(Comparator.comparingDouble(p -> distance(lat, lon, p.getLatitude(), p.getLongitude())))
                .limit(10) //약국조회개수
                .collect(Collectors.toList());
    }

    private double distance(double lat1, double lon1, double lat2, double lon2) { //계산하기
        double earthRadius = 6371; // km
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                        Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return earthRadius * c;
    }

    private PharmacyDTO transform(PharmacyEntity pharmacyEntity) { //변경폼
        return PharmacyDTO.builder()
                .id(pharmacyEntity.getPhar_id())
                .district(pharmacyEntity.getDistrict())
                .name(pharmacyEntity.getName())
                .name_en(pharmacyEntity.getPhar_name_en())
                .name_ch(pharmacyEntity.getPhar_name_ch())
                .name_ja(pharmacyEntity.getPhar_name_ja())
                .address(pharmacyEntity.getAddress())
                .phone(pharmacyEntity.getPhone())
                .english(pharmacyEntity.isEnglish())
                .chinese(pharmacyEntity.isChinese())
                .japanese(pharmacyEntity.isJapanese())
                .build();
    }


    // Method to get pharmacies by district
    public List<PharmacyDTO> getPharmaciesByName(String name) {
        return pharmacyRepository.findByNameContaining(name)
                .stream()
                .map(this::transform)
                .collect(Collectors.toList());
    }

    public List<PharmacyDTO> getPharmaciesByDistrict(String gu) {
        return pharmacyRepository.findByDistict(gu)
                .stream()
                .map(this::transform)
                .collect(Collectors.toList());
    }


}
