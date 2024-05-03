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

    // review
    @GetMapping("/{hospitalId}/reviews") // 특정 병원의 리뷰 조회
    public List<ReviewDto> getReviewsByHospitalId(@PathVariable("hospitalId") String hospitalId) {
        log.info("GET request received at /hospital/{}/reviews", hospitalId);
        return reviewService.getReviewsByHospitalId(hospitalId);
    }

    @PostMapping("/{hospitalId}/reviews") // 특정 병원에 리뷰 추가
    public ResponseEntity<ReviewDto> createReview(@PathVariable("hospitalId") String hospitalId, @RequestBody ReviewDto reviewDto) {
        log.info("POST request received at /hospital/{}/reviews", hospitalId);
        reviewDto.setHospitalId(hospitalId);
        ReviewDto createdReview = reviewService.createReview(reviewDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdReview);
    }

    @PutMapping("/{hospitalId}/reviews/{reviewId}") // 특정 병원의 리뷰 수정
    public ResponseEntity<ReviewDto> updateReview(@PathVariable("hospitalId") String hospitalId, @PathVariable("reviewId") Long reviewId, @RequestBody ReviewDto reviewDto) {
        log.info("PUT request received at /hospital/{}/reviews/{}", hospitalId, reviewId);
        if (!reviewDto.getHospitalId().equals(hospitalId)) {
            throw new IllegalArgumentException("Hospital ID in path does not match Hospital ID in request body");
        }
        ReviewDto updatedReview = reviewService.updateReview(reviewId, reviewDto);
        return ResponseEntity.ok(updatedReview);
    }

    @DeleteMapping("/{hospitalId}/reviews/{reviewId}") // 특정 병원의 리뷰 삭제
    public ResponseEntity<Void> deleteReview(@PathVariable("hospitalId") String hospitalId, @PathVariable("reviewId") Long reviewId) {
        log.info("DELETE request received at /hospital/{}/reviews/{}", hospitalId, reviewId);
        reviewService.deleteReview(reviewId);
        return ResponseEntity.noContent().build();
    }

}
