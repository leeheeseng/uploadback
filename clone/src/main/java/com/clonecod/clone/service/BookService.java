package com.clonecod.clone.service;

import com.clonecod.clone.dto.BookDetailDTO;
import com.clonecod.clone.dto.PagedResponse;
import com.clonecod.clone.dto.ReviewDTO;
import com.clonecod.clone.exception.ResourceNotFoundException;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface BookService {
    BookDetailDTO getBookDetails(Long bookId);
    /**
     * 📘 도서 상세 정보 조회
     * @param bookId 조회할 도서 ID
     * @return 도서 상세 정보 DTO
     * @throws ResourceNotFoundException 도서를 찾을 수 없는 경우
     */
    PagedResponse<ReviewDTO> getReviewsByBookId(Long bookId, Pageable pageable);
    /**
     * ✍️ 도서 리뷰 목록 조회 (최신순 정렬)
     * @param bookId 조회할 도서 ID
     * @return 리뷰 DTO 목록
     * @throws ResourceNotFoundException 도서를 찾을 수 없는 경우
     */

    /**
     * 📝 리뷰 등록
     * @param reviewDTO 등록할 리뷰 정보
     * @return 저장된 리뷰 DTO
     * @throws ResourceNotFoundException 도서를 찾을 수 없는 경우
     */
    ReviewDTO createReview(ReviewDTO reviewDTO) throws ResourceNotFoundException;

    /**
     * ⭐ 도서 평점 갱신 (모든 리뷰의 평균 평점 계산)
     * @param bookId 평점을 갱신할 도서 ID
     * @throws ResourceNotFoundException 도서를 찾을 수 없는 경우
     */
    void updateBookRating(Long bookId) throws ResourceNotFoundException;

    /**
     * 📚 전체 도서 목록 조회 (페이징 처리)
     * @param pageable 페이징 및 정렬 정보
     * @return 페이징 처리된 도서 목록
     */
    PagedResponse<BookDetailDTO> getAllBooks(Pageable pageable);

    /**
     * 📚 상위 카테고리별 도서 목록 조회 (페이징 처리)
     * @param topCategoryId 상위 카테고리 ID
     * @param pageable 페이징 및 정렬 정보
     * @return 페이징 처리된 도서 목록
     */
    PagedResponse<BookDetailDTO> getBooksByTopCategory(Long topCategoryId, Pageable pageable);

    /**
     * 📚 하위 카테고리별 도서 목록 조회 (페이징 처리)
     * @param subCategoryId 하위 카테고리 ID
     * @param pageable 페이징 및 정렬 정보
     * @return 페이징 처리된 도서 목록
     */
    PagedResponse<BookDetailDTO> getBooksBySubCategory(Long subCategoryId, Pageable pageable);
    BookDetailDTO updateBook(Long bookId, BookDetailDTO bookDetailDTO);
    /**
     * 📚 상위+하위 카테고리별 도서 목록 조회 (페이징 처리)
     * @param topCategoryId 상위 카테고리 ID
     * @param subCategoryId 하위 카테고리 ID
     * @param pageable 페이징 및 정렬 정보
     * @return 페이징 처리된 도서 목록
     */
    PagedResponse<BookDetailDTO> getBooksByTopAndSubCategory(Long topCategoryId, Long subCategoryId, Pageable pageable);
    BookDetailDTO updateCover(Long bookId, String coverUrl);
    BookDetailDTO findNextBookWithoutCover(Long bookId);
    List<BookDetailDTO> getAllBooks();
    PagedResponse<BookDetailDTO> searchBooks(String title, String author, String type, Pageable pageable);
}