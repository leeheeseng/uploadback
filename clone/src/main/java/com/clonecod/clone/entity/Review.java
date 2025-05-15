package com.clonecod.clone.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Table(name = "review")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_id")
    private Integer reviewId;

    @Column(name = "member_id", nullable = false)
    private Long memberId;  // 회원 ID

    @Column(name = "book_id", nullable = false)
    private Long bookId;  // 책 ID

    @Column(name = "Rating", nullable = false)
    private Integer rating;  // 평점

    @Column(name = "review_text", columnDefinition = "TEXT", nullable = false)
    private String reviewText;  // 리뷰 내용

    @Column(name = "Registration_date", nullable = false)
    private LocalDate registrationDate;  // 리뷰 등록일

    @PrePersist
    protected void onCreate() {
        this.registrationDate = LocalDate.now();
    }

    @ManyToOne(fetch = FetchType.LAZY)
@JoinColumn(name = "member_id", insertable = false, updatable = false)
private Member member;

@ManyToOne(fetch = FetchType.LAZY)
@JoinColumn(name = "book_id", insertable = false, updatable = false)
private BookDetail bookDetail;

}
