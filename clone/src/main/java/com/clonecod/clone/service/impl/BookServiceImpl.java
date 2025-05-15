package com.clonecod.clone.service.impl;

import com.clonecod.clone.dto.BookDetailDTO;
import com.clonecod.clone.dto.PagedResponse;
import com.clonecod.clone.dto.ReviewDTO;
import com.clonecod.clone.entity.BookDetail;
import com.clonecod.clone.entity.Review;
import com.clonecod.clone.exception.ResourceNotFoundException;
import com.clonecod.clone.repository.BookDetailRepository;
import com.clonecod.clone.repository.ReviewRepository;
import com.clonecod.clone.service.BookService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
@Slf4j
@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {

    private static final int MAX_PAGE_SIZE = 100;
    private static final Sort DEFAULT_SORT = Sort.by("bookId").ascending();

    private final BookDetailRepository bookDetailRepository;
    private final ReviewRepository reviewRepository;

    // 도서 상세 조회
    @Override
    @Transactional(readOnly = true)
    public BookDetailDTO getBookDetails(Long bookId) {
        log.info("도서 상세 조회 요청 - bookId: {}", bookId);
        return bookDetailRepository.findById(bookId)
                .map(this::convertToDTO)
                .orElseThrow(() -> new ResourceNotFoundException("해당 ID의 책을 찾을 수 없습니다: " + bookId));
    }

    // 도서 리뷰 조회
    @Override
    @Transactional(readOnly = true)
    public PagedResponse<ReviewDTO> getReviewsByBookId(Long bookId, Pageable pageable) {
        log.info("도서 리뷰 리스트 조회 - bookId: {}, 페이지: {}", bookId, pageable);
        validateBookExists(bookId);
        Page<Review> reviewPage = reviewRepository.findByBookId(bookId, pageable);
        List<ReviewDTO> reviewDTOs = reviewPage.getContent().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());

        return PagedResponse.<ReviewDTO>builder()
                .content(reviewDTOs)
                .currentPage(reviewPage.getNumber() + 1)
                .pageSize(reviewPage.getSize())
                .totalElements(reviewPage.getTotalElements())
                .totalPages(reviewPage.getTotalPages())
                .build();
    }

    // 리뷰 등록
    @Override
    @Transactional
    public ReviewDTO createReview(ReviewDTO reviewDTO) {
        log.info("리뷰 등록 요청 - bookId: {}", reviewDTO.getBookId());
        validateBookExists(reviewDTO.getBookId());

        Review review = Review.builder()
                .memberId(reviewDTO.getMemberId())
                .bookId(reviewDTO.getBookId())
                .rating(reviewDTO.getRating())
                .reviewText(reviewDTO.getReviewText())
                .registrationDate(LocalDate.now())
                .build();

        Review savedReview = reviewRepository.save(review);
        updateBookRating(reviewDTO.getBookId());

        return convertToDTO(savedReview);
    }

    // 책의 평점 업데이트
    @Override
    @Transactional
    public void updateBookRating(Long bookId) {
        log.info("평균 평점 업데이트 실행 - bookId: {}", bookId);
        BookDetail book = getBookOrThrow(bookId);

        BigDecimal avgRating = reviewRepository.findByBookIdOrderByRegistrationDateDesc(bookId)
                .stream()
                .collect(Collectors.collectingAndThen(
                        Collectors.averagingInt(Review::getRating),
                        average -> BigDecimal.valueOf(average).setScale(2, RoundingMode.HALF_UP)
                ));

        book.setRating(avgRating);
        bookDetailRepository.save(book);
    }

    // 카테고리별 도서 조회
    @Override
    @Transactional(readOnly = true)
    public PagedResponse<BookDetailDTO> getBooksByTopAndSubCategory(Long topCategoryId, Long subCategoryId, Pageable pageable) {
        log.info("카테고리 조회 - topCategoryId: {}, subCategoryId: {}", topCategoryId, subCategoryId);
        Page<BookDetail> page = bookDetailRepository.findByTopCategoryIdAndSubcategoryId(
                topCategoryId, subCategoryId, createValidPageRequest(pageable));
        return convertToPagedResponse(page);
    }

    // 상위 카테고리별 도서 조회
    @Override
    @Transactional(readOnly = true)
    public PagedResponse<BookDetailDTO> getBooksByTopCategory(Long topCategoryId, Pageable pageable) {
        log.info("상위 카테고리 조회 - topCategoryId: {}", topCategoryId);
        Page<BookDetail> page = bookDetailRepository.findByTopCategoryId(
                topCategoryId, createValidPageRequest(pageable));
        return convertToPagedResponse(page);
    }

    // 하위 카테고리별 도서 조회
    @Override
    @Transactional(readOnly = true)
    public PagedResponse<BookDetailDTO> getBooksBySubCategory(Long subCategoryId, Pageable pageable) {
        log.info("하위 카테고리 조회 - subCategoryId: {}", subCategoryId);
        Page<BookDetail> page = bookDetailRepository.findBySubcategoryId(
                subCategoryId, createValidPageRequest(pageable));
        return convertToPagedResponse(page);
    }

    // 모든 도서 조회
    @Override
    @Transactional(readOnly = true)
    public PagedResponse<BookDetailDTO> getAllBooks(Pageable pageable) {
        log.info("전체 도서 리스트 조회");
        Page<BookDetail> page = bookDetailRepository.findAll(
                createValidPageRequest(pageable));
        return convertToPagedResponse(page);
    }

    // 커버가 없는 도서 1권 순차 조회
    @Override
    @Transactional(readOnly = true)
    public BookDetailDTO findNextBookWithoutCover(Long bookId) {
        log.info("도서 순차 조회 요청 - 이전 책 ID: {}", bookId);

        // bookId 이후의 책을 조회 (bookId가 null이면 처음부터 조회)
        List<BookDetail> books = (bookId == null) ? bookDetailRepository.findAll() :
                               bookDetailRepository.findByBookIdGreaterThanOrderByBookIdAsc(bookId);

        // 책이 존재하면 반환
        if (!books.isEmpty()) {
            return convertToDTO(books.get(0)); // 첫 번째 도서를 반환
        }

        // 더 이상 반환할 도서가 없으면 null 반환
        return null;
    }

    // 책 전체 정보 업데이트
    @Override
    @Transactional
    public BookDetailDTO updateBook(Long bookId, BookDetailDTO bookDetailDTO) {
        log.info("책 정보 업데이트 요청 - bookId: {}", bookId);

        // 책을 조회하고 없으면 예외 발생
        BookDetail book = getBookOrThrow(bookId);

        // 책의 모든 필드 업데이트
        book.setSubcategoryId(bookDetailDTO.getSubcategoryId());
        book.setTopCategoryId(bookDetailDTO.getTopCategoryId());
        book.setPrice(bookDetailDTO.getPrice());
        book.setWriter(bookDetailDTO.getWriter());
        book.setPublisher(bookDetailDTO.getPublisher());
        book.setBookIndex(bookDetailDTO.getBookIndex());
        book.setCover(bookDetailDTO.getCover());
        book.setWritersComment(bookDetailDTO.getWritersComment());
        book.setRating(bookDetailDTO.getRating());
        book.setTitle(bookDetailDTO.getTitle());
        book.setPublicationDate(bookDetailDTO.getPublicationDate());
        book.setDiscountRate(bookDetailDTO.getDiscountRate());

        // 업데이트된 책을 DB에 저장하고 DTO로 반환
        BookDetail updatedBook = bookDetailRepository.save(book);
        return convertToDTO(updatedBook);
    }

    // 커버 업데이트
    @Override
    @Transactional
    public BookDetailDTO updateCover(Long bookId, String coverUrl) {
        log.info("책 커버 업데이트 요청 - bookId: {}, coverUrl: {}", bookId, coverUrl);
        BookDetail book = getBookOrThrow(bookId);
        
        book.setCover(coverUrl);
        bookDetailRepository.save(book);
        
        return convertToDTO(book);
    }

    // 존재하는 책 조회
    private BookDetail getBookOrThrow(Long bookId) {
        return bookDetailRepository.findById(bookId)
                .orElseThrow(() -> new ResourceNotFoundException("해당 ID의 책을 찾을 수 없습니다: " + bookId));
    }

    // 리뷰 DTO로 변환
    private ReviewDTO convertToDTO(Review review) {
        return ReviewDTO.builder()
                .reviewId(review.getReviewId())
                .memberId(review.getMemberId())
                .bookId(review.getBookId())
                .rating(review.getRating())
                .reviewText(review.getReviewText())
                .registrationDate(review.getRegistrationDate())
                .build();
    }

    // 책 DTO로 변환
    private BookDetailDTO convertToDTO(BookDetail book) {
        return BookDetailDTO.builder()
                .bookId(book.getBookId())
                .subcategoryId(book.getSubcategoryId())
                .topCategoryId(book.getTopCategoryId())
                .price(book.getPrice())
                .writer(book.getWriter())
                .publisher(book.getPublisher())
                .bookIndex(book.getBookIndex())
                .cover(book.getCover())
                .WritersComment(book.getWritersComment())
                .rating(book.getRating())
                .title(book.getTitle())
                .publicationDate(book.getPublicationDate())
                .discountRate(book.getDiscountRate())
                .build();
    }

    // 페이지 응답 DTO로 변환
    private PagedResponse<BookDetailDTO> convertToPagedResponse(Page<BookDetail> page) {
        return PagedResponse.<BookDetailDTO>builder()
                .content(page.getContent().stream().map(this::convertToDTO).collect(Collectors.toList()))
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .currentPage(page.getNumber() + 1)
                .pageSize(page.getSize())
                .build();
    }

    // 유효한 페이지 요청 생성
    private PageRequest createValidPageRequest(Pageable pageable) {
        int pageSize = Math.min(pageable.getPageSize(), MAX_PAGE_SIZE);
        Sort sort = pageable.getSort().isSorted() ? pageable.getSort() : DEFAULT_SORT;
        return PageRequest.of(pageable.getPageNumber(), pageSize, sort);
    }

    // 책 존재 여부 체크
    private void validateBookExists(Long bookId) {
        if (!bookDetailRepository.existsById(bookId)) {
            throw new ResourceNotFoundException("해당 ID의 책을 찾을 수 없습니다: " + bookId);
        }
    }

    @Override
@Transactional(readOnly = true)
public List<BookDetailDTO> getAllBooks() {
    log.info("전체 도서 리스트 조회 - 페이징 없이 전체 반환");
    List<BookDetail> books = bookDetailRepository.findAll();
    return books.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
}




@Override
@Transactional(readOnly = true)
public PagedResponse<BookDetailDTO> searchBooks(String title, String author, String type, Pageable pageable) {
    log.info("도서 검색 요청 - 제목: {}, 저자: {}, 검색 유형: {}", title, author, type);

    Page<BookDetail> bookPage;

    // type이 "Writer"일 경우, 저자(author)만 검색
    if ("Writer".equals(type)) {
        if (!author.isEmpty()) {
            bookPage = bookDetailRepository.findByWriterContaining(author, createValidPageRequest(pageable));
        } else {
            bookPage = bookDetailRepository.findAll(createValidPageRequest(pageable));
        }
    } else {
        // 기존 제목과 저자를 기준으로 검색
        if (!title.isEmpty() && !author.isEmpty()) {
            // 제목과 저자 둘 다 있을 때
            bookPage = bookDetailRepository.findByTitleContainingAndWriterContaining(title, author, createValidPageRequest(pageable));
        } else if (!title.isEmpty()) {
            // 제목만 있을 때
            bookPage = bookDetailRepository.findByTitleContaining(title, createValidPageRequest(pageable));
        } else if (!author.isEmpty()) {
            // 저자만 있을 때
            bookPage = bookDetailRepository.findByWriterContaining(author, createValidPageRequest(pageable));
        } else {
            // 둘 다 비었을 때 전체 도서 반환
            bookPage = bookDetailRepository.findAll(createValidPageRequest(pageable));
        }
    }

    return convertToPagedResponse(bookPage);
}


}
