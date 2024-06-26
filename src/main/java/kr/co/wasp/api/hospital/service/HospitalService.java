package kr.co.wasp.api.hospital.service;

import kr.co.wasp.api.hospital.dto.HospitalDto;
import kr.co.wasp.api.hospital.entity.Hospital;
import kr.co.wasp.api.hospital.entity.Nation;
import kr.co.wasp.api.hospital.repository.HospitalRepository;
import kr.co.wasp.api.hospital.repository.NationRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import org.springframework.beans.factory.annotation.Autowired;

import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
public class HospitalService {
    private final HospitalRepository hospitalRepository;
    private final NationRepository nationRepository;

    @Autowired
    public HospitalService(HospitalRepository hospitalRepository, NationRepository nationRepository) {
        this.hospitalRepository = hospitalRepository;
        this.nationRepository = nationRepository;
    }


    // 병원 전체 조회
    public List<HospitalDto> getAllHospitals() {
        List<Hospital> hospitals = hospitalRepository.findAll();
        return hospitals.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    // 병원 key 번호로 개별 조회
    public HospitalDto getHospitalByRegisterNum(String key) {
        Hospital hospital = hospitalRepository.findHospitalByHospital_register_num(key);
        if (hospital == null) {
            throw new IllegalArgumentException("병원을 찾을 수 없습니다. 등록 번호: " + key);
        }
        return convertToDto(hospital);
    }

    // 구 별로 조회
    public List<HospitalDto> getHospitalsByGu(String hospital_gu) {
        List<Hospital> hospitals = hospitalRepository.findByHospital_gu(hospital_gu);
        return hospitals.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    // 병원 카테고리 별로 조회
    public List<HospitalDto> getHospitalsByCategory(String category) {
        List<Hospital> hospitals = hospitalRepository.findByHospital_category(category);
        return hospitals.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    // 키워드로 병원 검색
    public List<HospitalDto> findHospitalsByKeyword(String keyword) {
        List<Hospital> hospitals = hospitalRepository.findHospitalsByKeyword(keyword.toLowerCase());
        return hospitals.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    // Nation 별로 해당되는 병원들을 출력
    public List<HospitalDto> getHospitalsByNation(String nationName) {
        Nation nation = nationRepository.findByName(nationName);
        if (nation == null) {
            throw new IllegalArgumentException("해당 국가를 찾을 수 없습니다: " + nationName);
        }
        List<Hospital> hospitals = hospitalRepository.findByHospital_nationsContaining(nationName);
        return hospitals.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    // 위,경도,국가 -> 가까운 10개 병원 조회
    public List<HospitalDto> getHospitalsByLocationAndNation(double lat, double lon, String nationName) {
        List<Hospital> hospitals;
        if (nationName != null) {
            hospitals = hospitalRepository.findByHospital_nationsContaining(nationName);
        } else {
            hospitals = hospitalRepository.findAll();
        }
        return findNearestHospitals(lat, lon, hospitals);
    }

    private List<HospitalDto> findNearestHospitals(double lat, double lon, List<Hospital> hospitals) {
        List<Hospital> validHospitals = hospitals.stream()
                .filter(h -> h.getHospital_latitude() != null && h.getHospital_longitude() != null)
                .collect(Collectors.toList());

        return validHospitals.stream()
                .sorted(Comparator.comparingDouble(h -> calculateDistance(lat, lon, h.getHospital_latitude(), h.getHospital_longitude())))
                .limit(10)
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    private double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        double earthRadius = 6371; // km
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                        Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return earthRadius * c;
    }


    private HospitalDto convertToDto(Hospital hospital) {
        return HospitalDto.builder()
                .hospital_key(hospital.getHospital_key())
                .hospital_register_num(hospital.getHospital_register_num())
                .hospital_name(hospital.getHospital_name())
                .hospital_name_en(hospital.getHospital_name_en())
                .hospital_name_ch(hospital.getHospital_name_ch())
                .hospital_name_ja(hospital.getHospital_name_ja())
                .hospital_category(hospital.getHospital_category())
                .hospital_si(hospital.getHospital_si())
                .hospital_gu(hospital.getHospital_gu())
                .hospital_dong(hospital.getHospital_dong())
                .hospital_ceo(hospital.getHospital_ceo())
                .hospital_address(hospital.getHospital_address())
                .hospital_latitude(hospital.getHospital_latitude())
                .hospital_longitude(hospital.getHospital_longitude())
                .nations(hospital.getHospital_nations().stream().map(Nation::getName).collect(Collectors.toSet()))
                .build();
    }

    public void saveHospitalAndNations(Hospital hospital) {
        hospitalRepository.save(hospital);

        Set<Nation> nations = hospital.getHospital_nations();
        if (nations != null && !nations.isEmpty()) {
            nationRepository.saveAll(nations);
        }
    }
}
