package com.clonecod.clone.repository;

import com.clonecod.clone.entity.BookDetail;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface BookDetailRepository extends JpaRepository<BookDetail, Long> {
    
    // 평균 평점 계산 (네이티브 쿼리)
    @Query(value = "SELECT AVG(r.rating) FROM review r WHERE r.book_id = :bookId", nativeQuery = true)
    Optional<Double> calculateAverageRating(@Param("bookId") Long bookId);

    // 평점 업데이트
    @Modifying
    @Transactional
    @Query("UPDATE BookDetail b SET b.rating = :rating WHERE b.bookId = :bookId")
    void updateRating(@Param("bookId") Long bookId, @Param("rating") BigDecimal rating);
    Page<BookDetail> findByBookId(Long bookId, Pageable pageable); 

    // 카테고리별 조회
    Page<BookDetail> findByTopCategoryIdAndSubcategoryId(Long topCategoryId, Long subCategoryId, Pageable pageable);
    Page<BookDetail> findByTopCategoryId(Long topCategoryId, Pageable pageable);
    Page<BookDetail> findBySubcategoryId(Long subCategoryId, Pageable pageable);
    List<BookDetail> findByBookIdIn(List<Long> bookIds);
    // 가격 범위 검색
    Page<BookDetail> findByPriceBetween(Integer minPrice, Integer maxPrice, Pageable pageable);

    // 할인율 기준 검색
    Page<BookDetail> findByDiscountRateGreaterThanEqual(Integer minDiscountRate, Pageable pageable);

    // 제목 검색 (대소문자 무시)
    Page<BookDetail> findByTitleContainingIgnoreCase(String keyword, Pageable pageable);
    Optional<BookDetail> findFirstByCoverIsNullOrCoverEqualsOrderByBookIdAsc(String emptyString);
    List<BookDetail> findByBookIdGreaterThanOrderByBookIdAsc(Long bookId);
    Page<BookDetail> findByTitleContaining(String title, Pageable pageable);
    Page<BookDetail> findByWriterContaining(String writer, Pageable pageable);
    Page<BookDetail> findByTitleContainingAndWriterContaining(String title, String writer, Pageable pageable);
    

}