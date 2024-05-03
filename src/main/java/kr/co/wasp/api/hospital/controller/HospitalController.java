package kr.co.wasp.api.hospital.controller;


import kr.co.wasp.api.hospital.dto.HospitalDto;
import kr.co.wasp.api.hospital.dto.ReviewDto;
import kr.co.wasp.api.hospital.service.HospitalService;
import kr.co.wasp.api.hospital.service.ReviewService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/hospitals")
public class HospitalController {
    private final HospitalService hospitalService;
    private final ReviewService reviewService;

    @Autowired
    public HospitalController(HospitalService hospitalService, ReviewService reviewService) {
        this.hospitalService = hospitalService;
        this.reviewService = reviewService;
    }


    @GetMapping("") // 전체 병원 조회
    public List<HospitalDto> getAllHospitals() {
        return hospitalService.getAllHospitals();
    }

    @GetMapping("/{key}") // hospital_key로 개별 조회
    public HospitalDto getHospitalByRegisterNum(@PathVariable("key") String key) {
//        log.info("GET request received at /hospital/{}", registerNum);
        return hospitalService.getHospitalByRegisterNum(key);
    }

    @GetMapping("/gu/{gu}") // 구로 조회 ex. 노원구
    public List<HospitalDto> getHospitalsByGu(@PathVariable("gu") String gu) {
        return hospitalService.getHospitalsByGu(gu);
    }

    @GetMapping("/category/{category}") // 카테고리별로 조회
    public List<HospitalDto> getHospitalsByCategory(@PathVariable("category") String category) {
        return hospitalService.getHospitalsByCategory(category);
    }

    @GetMapping("/search") // 키워드로 병원 검색
    public List<HospitalDto> findHospitalsByKeyword(@RequestParam("keyword") String keyword) {
//        log.info("GET request received at /hospital/search?keyword={}", keyword);
        return hospitalService.findHospitalsByKeyword(keyword);
    }

    @GetMapping("/nation/{nationName}") // 국가별로 조회
    public List<HospitalDto> getHospitalsByNation(@PathVariable("nationName") String nationName) {
//        log.info("GET request received at /hospital/nation/{}", nationName);
        return hospitalService.getHospitalsByNation(nationName);
    }

}
