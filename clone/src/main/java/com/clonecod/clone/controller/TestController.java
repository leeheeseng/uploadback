package com.clonecod.clone.controller;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.clonecod.clone.entity.BookDetail;
import com.clonecod.clone.repository.*;

@RestController
@RequestMapping("/api/test")
public class TestController {

    @Autowired
    private BookDetailRepository bookDetailRepository;
    @Autowired
    private EventDetailRepository eventDetailRepository;
    @Autowired
    private EventRepository eventRepository;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private NoticeRepository noticeRepository;
    @Autowired
    private PurchaseRepository purchaseRepository;
    @Autowired
    private ReviewRepository reviewRepository;
    @Autowired
    private ShoppingCartRepository shoppingCartRepository;
    @Autowired
    private SubCategoryRepository subCategoryRepository;
    @Autowired
    private TopCategoryRepository topCategoryRepository;

    // 테스트용 엔드포인트
    @GetMapping("/test/all")
    public String testAllRepositories() {
       // BookDetailRepository 테스트
        bookDetailRepository.calculateAverageRating(1L);
        bookDetailRepository.updateRating(1L, BigDecimal.valueOf(4.5));
        bookDetailRepository.findByBookId(1L, PageRequest.of(0, 5));
        bookDetailRepository.findByTopCategoryIdAndSubcategoryId(1L, 2L, PageRequest.of(0, 5));
        bookDetailRepository.findByTopCategoryId(1L, PageRequest.of(0, 5));
        bookDetailRepository.findBySubcategoryId(2L, PageRequest.of(0, 5));
        bookDetailRepository.findByBookIdIn(Arrays.asList(1L, 2L, 3L));
        bookDetailRepository.findByPriceBetween(1000, 50000, PageRequest.of(0, 5));
        bookDetailRepository.findByDiscountRateGreaterThanEqual(10, PageRequest.of(0, 5));
        bookDetailRepository.findByTitleContainingIgnoreCase("테스트", PageRequest.of(0, 5));
        Optional<BookDetail> detail = bookDetailRepository.findFirstByCoverIsNullOrCoverEqualsOrderByBookIdAsc("");
        bookDetailRepository.findByBookIdGreaterThanOrderByBookIdAsc(1L);

   

        // EventDetailRepository 테스트
        eventDetailRepository.findByEvent_eventId(1L);

        // EventRepository 테스트
        eventRepository.findFirstByCoverIsNullOrCover("");
        eventRepository.findNextByIdAndCoverIsNullOrEmpty(1L);
        eventRepository.findFirstByOrderByEventIdAsc();
        eventRepository.findFirstByEventIdGreaterThanOrderByEventIdAsc(1L);

        // MemberRepository 테스트
        memberRepository.existsByLoginId("user123");
        memberRepository.findByLoginId("user123");

        // NoticeRepository 테스트
        noticeRepository.findAll();

        // PurchaseRepository 테스트
        purchaseRepository.findByMemberId(1L);

        // ReviewRepository 테스트
        reviewRepository.findByBookId(1L, PageRequest.of(0, 5));
        reviewRepository.findByBookIdOrderByRegistrationDateDesc(1L);

        // ShoppingCartRepository 테스트
        shoppingCartRepository.findByMemberId(1L);
 

        // SubCategoryRepository 테스트
        subCategoryRepository.findByTopCategoryId(1);
        subCategoryRepository.findByTopCategoryIdWithQuery(1);

        // TopCategoryRepository 테스트
        topCategoryRepository.findAllWithSubCategories();

        return "All repositories have been tested!";
    }
}
