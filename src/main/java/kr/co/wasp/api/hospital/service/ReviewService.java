package kr.co.wasp.api.hospital.service;

import kr.co.wasp.api.hospital.dto.ReviewDto;
import kr.co.wasp.api.hospital.entity.Hospital;
import kr.co.wasp.api.hospital.entity.Review;
import kr.co.wasp.api.hospital.repository.HospitalRepository;
import kr.co.wasp.api.hospital.repository.ReviewRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final HospitalRepository hospitalRepository;

    @Autowired
    public ReviewService(ReviewRepository reviewRepository, HospitalRepository hospitalRepository) {
        this.reviewRepository = reviewRepository;
        this.hospitalRepository = hospitalRepository;
    }

    public List<ReviewDto> getReviewsByHospitalId(String hospitalId) {
        List<Review> reviews = reviewRepository.findByHospitalId(hospitalId);
        return reviews.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public ReviewDto createReview(ReviewDto reviewDto) {
        Hospital hospital = hospitalRepository.findById(reviewDto.getHospitalId())
                .orElseThrow(() -> new IllegalArgumentException("Hospital not found with id: " + reviewDto.getHospitalId()));

        Review review = new Review();
        review.setComment(reviewDto.getComment());
        review.setRating(reviewDto.getRating());
        review.setHospital(hospital);
        review.setReviewer(reviewDto.getReviewer());

        Review savedReview = reviewRepository.save(review);
        return convertToDto(savedReview);
    }

    public ReviewDto updateReview(Long reviewId, ReviewDto reviewDto) {
        Review existingReview = reviewRepository.findById(reviewId.toString())
                .orElseThrow(() -> new IllegalArgumentException("Review not found with id: " + reviewId));

        existingReview.setComment(reviewDto.getComment());
        existingReview.setRating(reviewDto.getRating());
        existingReview.setReviewer(reviewDto.getReviewer());


        Review updatedReview = reviewRepository.save(existingReview);
        return convertToDto(updatedReview);
    }

    public void deleteReview(Long reviewId) {
        reviewRepository.deleteById(reviewId.toString());
    }

    private ReviewDto convertToDto(Review review) {
        ReviewDto reviewDto = new ReviewDto();
        reviewDto.setId(review.getId());
        reviewDto.setComment(review.getComment());
        reviewDto.setRating(review.getRating());
        reviewDto.setReviewer(review.getReviewer());
        reviewDto.setHospitalId(review.getHospital().getHospital_key());
        return reviewDto;
    }
}

