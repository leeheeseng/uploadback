package com.clonecod.clone.repository;

import com.clonecod.clone.entity.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Integer> {

    // 책 ID로 리뷰를 페이징 처리하여 조회
    Page<Review> findByBookId(Long bookId, Pageable pageable);

    // 최신 등록일 순으로 정렬된 리뷰를 조회
    List<Review> findByBookIdOrderByRegistrationDateDesc(Long bookId);
}
