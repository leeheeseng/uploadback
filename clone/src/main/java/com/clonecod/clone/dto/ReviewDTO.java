package com.clonecod.clone.dto;

import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDate;
import com.fasterxml.jackson.annotation.JsonFormat;

@Getter
@AllArgsConstructor // 모든 필드를 초기화할 수 있는 생성자 추가
@Builder
@NoArgsConstructor // 기본 생성자 추가
public class ReviewDTO {

    private Integer reviewId;

    @NotNull(message = "회원 ID는 필수 항목입니다.")
    private Long memberId;

    @NotNull(message = "책 ID는 필수 항목입니다.")
    private Long bookId;

    @NotNull(message = "평점은 필수 항목입니다.")
    @Min(value = 1, message = "평점은 1점 이상이어야 합니다.")
    @Max(value = 5, message = "평점은 5점 이하여야 합니다.")
    private Integer rating;

    @NotBlank(message = "리뷰 내용은 필수 항목입니다.")
    @Size(min = 10, message = "리뷰는 최소 10자 이상이어야 합니다.")
    private String reviewText;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate registrationDate;
}
