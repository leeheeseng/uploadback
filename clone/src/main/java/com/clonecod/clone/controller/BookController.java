package com.clonecod.clone.controller;

import com.clonecod.clone.dto.BookDetailDTO;
import com.clonecod.clone.dto.PagedResponse;
import com.clonecod.clone.dto.ReviewDTO;
import com.clonecod.clone.service.BookService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/detail")
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;
    private static final Logger logger = LoggerFactory.getLogger(BookController.class);

    /**
     * 📘 책 상세 정보 조회
     * @param bookId 조회할 도서 ID
     * @return 도서 상세 정보 DTO
     */
    @GetMapping("/{bookId}")
    public ResponseEntity<BookDetailDTO> getBookDetails(@PathVariable Long bookId) {
        try {
            BookDetailDTO bookDetail = bookService.getBookDetails(bookId);
            return ResponseEntity.ok(bookDetail);
        } catch (Exception e) {
            logger.error("Error getting book details for bookId: {}", bookId, e);
            throw e;
        }
    }

    /**
     * ✍ 리뷰 목록 조회 (등록일 최신순)
     * @param bookId 조회할 도서 ID
     * @param page 페이지 번호
     * @param size 페이지 크기
     * @return 페이징 처리된 리뷰 목록
     */
    @GetMapping("/{bookId}/reviews")
    public ResponseEntity<PagedResponse<ReviewDTO>> getReviewsByBookId(
            @PathVariable Long bookId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            Pageable pageable = PageRequest.of(page, size, Sort.by("registrationDate").descending());
            PagedResponse<ReviewDTO> reviews = bookService.getReviewsByBookId(bookId, pageable);
            return ResponseEntity.ok(reviews);
        } catch (Exception e) {
            logger.error("Error getting reviews for bookId: {}", bookId, e);
            throw e;
        }
    }

    /**
     * 📝 리뷰 등록
     * @param bookId 리뷰를 등록할 도서 ID
     * @param reviewDTO 등록할 리뷰 정보
     * @return 저장된 리뷰 DTO
     */
    @PostMapping("/{bookId}/reviews")
    public ResponseEntity<ReviewDTO> createReview(
            @PathVariable Long bookId,
            @RequestBody ReviewDTO reviewDTO) {

       

        try {
            ReviewDTO createdReview = bookService.createReview(reviewDTO);
            return ResponseEntity.ok(createdReview);
        } catch (Exception e) {
            logger.error("리뷰 생성 중 오류 발생 - bookId: {}, 에러: {}", bookId, e.getMessage(), e);
            throw new RuntimeException("리뷰 생성 중 문제가 발생했습니다.", e);
        }
    }

    /**
     * 📚 카테고리별 도서 리스트 조회 (페이징 + 필터링)
     * @param topCategoryId 상위 카테고리 ID
     * @param subCategoryId 하위 카테고리 ID
     * @param sort 정렬 기준 (기본값: bookId,asc)
     * @param period 기간 필터 (optional)
     * @param page 페이지 번호
     * @param size 페이지 크기
     * @return 페이징 처리된 도서 목록
     */
    @GetMapping("/category-products")
    public ResponseEntity<PagedResponse<BookDetailDTO>> getBooksByCategory(
            @RequestParam(required = false) Long topCategoryId,
            @RequestParam(required = false) Long subCategoryId,
            @RequestParam(defaultValue = "bookId,asc") String sort,
            @RequestParam(required = false) String period,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        
        logger.info("카테고리별 도서 조회 - 상위: {}, 하위: {}, 페이지: {}, 크기: {}, 정렬: {}, 기간: {}",
                topCategoryId, subCategoryId, page, size, sort, period);
        
        try {
            // period 파라미터에 따라 sorting 재설정
            if ("rating".equals(period)) {
                sort = "rating,desc";            // 평점순
            } else if ("discount".equals(period)) {
                sort = "discountRate,desc";      // 할인율순
            } else if ("new".equals(period)) {
                sort = "publicationDate,desc";   // 신상품순
            }

            // 정렬 파라미터 처리
            String[] sortParams = sort.split(",");
            String sortField = sortParams[0];
            Sort.Direction direction = sortParams.length > 1 && "desc".equalsIgnoreCase(sortParams[1])
                    ? Sort.Direction.DESC : Sort.Direction.ASC;

            PageRequest pageable = PageRequest.of(page, size, Sort.by(direction, sortField));

            PagedResponse<BookDetailDTO> result;
            if (topCategoryId != null && subCategoryId != null) {
                result = bookService.getBooksByTopAndSubCategory(topCategoryId, subCategoryId, pageable);
            } else if (topCategoryId != null) {
                result = bookService.getBooksByTopCategory(topCategoryId, pageable);
            } else if (subCategoryId != null) {
                result = bookService.getBooksBySubCategory(subCategoryId, pageable);
            } else {
                result = bookService.getAllBooks(pageable);
            }

            return ResponseEntity.ok(result);
        } catch (Exception e) {
            logger.error("도서 조회 중 오류 발생", e);
            throw e;
        }
    }
    @GetMapping("/empty-cover")
    public ResponseEntity<BookDetailDTO> getBookWithoutCover(@RequestParam(required = false) Long bookId) {
        BookDetailDTO book = bookService.findNextBookWithoutCover(bookId);
        return book != null ? ResponseEntity.ok(book) : ResponseEntity.noContent().build();
    }
    
    @PutMapping("/books/{bookId}/cover")
    public ResponseEntity<BookDetailDTO> updateCover(@PathVariable Long bookId, @RequestBody Map<String, String> request) {
        try {
            String coverUrl = request.get("cover");
            BookDetailDTO updatedBook = bookService.updateCover(bookId, coverUrl);
            return ResponseEntity.ok(updatedBook);
        } catch (Exception e) {
            logger.error("Error updating cover for bookId: {}", bookId, e);
            throw new RuntimeException("커버 업데이트 중 오류 발생", e);
        }
    }
     // 책의 전체 정보를 업데이트
     @PutMapping("/books/{bookId}")
     public ResponseEntity<BookDetailDTO> updateBook(@PathVariable Long bookId, @RequestBody BookDetailDTO bookDetailDTO) {
         try {
             BookDetailDTO updatedBook = bookService.updateBook(bookId, bookDetailDTO);
             return ResponseEntity.ok(updatedBook);
         } catch (Exception e) {
             logger.error("Error updating book details for bookId: {}", bookId, e);
             throw new RuntimeException("책 정보 업데이트 중 오류 발생", e);
         }
     }

     @GetMapping
public ResponseEntity<List<BookDetailDTO>> getAllBooks() {
    try {
        List<BookDetailDTO> books = bookService.getAllBooks();
        return ResponseEntity.ok(books);
    } catch (Exception e) {
        logger.error("전체 책 목록 조회 실패", e);
        throw new RuntimeException("전체 책 조회 중 오류 발생", e);
    }
}@GetMapping("/search")
public PagedResponse<BookDetailDTO> searchBooks(
        @RequestParam(value = "title", defaultValue = "") String title,
        @RequestParam(value = "Writer", defaultValue = "") String Writer, // 기존의 author는 Writer를 처리할 수 있도록 변경
        @RequestParam(value = "type", defaultValue = "title") String type, // type을 쿼리 파라미터로 받아옴
        Pageable pageable) {

    logger.info("도서 검색 요청 - 제목: {}, 저자: {}, 타입: {}", title, Writer, type);

    // 검색 메서드 호출
    return bookService.searchBooks(title, Writer, type, pageable);
}


}
