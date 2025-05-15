package com.clonecod.clone.converter;

import com.clonecod.clone.entity.BookDetail;
import com.clonecod.clone.entity.Review;
import com.clonecod.clone.dto.ReviewDTO;

public class ReviewConverter {

    /**
     * Review 엔티티를 ReviewDTO로 변환
     * @param review 변환할 Review 엔티티
     * @return 변환된 ReviewDTO 객체
     * @throws IllegalArgumentException review가 null인 경우
     */
    public static ReviewDTO convertToDTO(Review review) {
        if (review == null) {
            throw new IllegalArgumentException("Review cannot be null");
        }

        return ReviewDTO.builder()
                .reviewId(review.getReviewId())
                .memberId(review.getMemberId())  // 이제 memberId를 직접 사용
                .bookId(review.getBookId())  // 이제 bookId를 직접 사용
                .rating(review.getRating())
                .reviewText(review.getReviewText())
                .registrationDate(review.getRegistrationDate())
                .build();
    }

    /**
     * ReviewDTO를 Review 엔티티로 변환
     * @param reviewDTO 변환할 ReviewDTO
     * @return 변환된 Review 엔티티
     * @throws IllegalArgumentException reviewDTO가 null인 경우
     */
    public static Review convertToEntity(ReviewDTO reviewDTO) {
        if (reviewDTO == null) {
            throw new IllegalArgumentException("ReviewDTO cannot be null");
        }

        return Review.builder()
                .reviewId(reviewDTO.getReviewId())
                .memberId(reviewDTO.getMemberId())  // memberId만 설정
                .bookId(reviewDTO.getBookId())  // bookId만 설정
                .rating(reviewDTO.getRating())
                .reviewText(reviewDTO.getReviewText())
                .registrationDate(reviewDTO.getRegistrationDate())
                .build();
    }
}
