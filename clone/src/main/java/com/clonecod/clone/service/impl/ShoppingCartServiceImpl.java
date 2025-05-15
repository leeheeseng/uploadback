package com.clonecod.clone.service.impl;

import com.clonecod.clone.dto.BookDetailDTO;
import com.clonecod.clone.entity.BookDetail;
import com.clonecod.clone.entity.ShoppingCart;
import com.clonecod.clone.repository.BookDetailRepository;
import com.clonecod.clone.repository.ShoppingCartRepository;
import com.clonecod.clone.service.ShoppingCartService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ShoppingCartServiceImpl implements ShoppingCartService {

    private final ShoppingCartRepository cartRepository;
    private final BookDetailRepository bookDetailRepository;

    private static final Logger logger = LoggerFactory.getLogger(ShoppingCartServiceImpl.class);

    // 회원 ID로 장바구니의 BookDetail 목록 조회
    @Override
    public List<BookDetailDTO> getBooksInCart(Long memberId) {
        List<ShoppingCart> cartItems = cartRepository.findByMemberId(memberId);
        List<Long> bookIds = cartItems.stream()
                                      .map(ShoppingCart::getBookId)
                                      .collect(Collectors.toList());

        if (bookIds.isEmpty()) {
            logger.info("회원 ID {}의 장바구니에 상품이 없습니다.", memberId);
            return Collections.emptyList();
        }

        // BookDetail 객체를 가져온 뒤, BookDetailDTO로 변환
        List<BookDetail> bookDetails = bookDetailRepository.findByBookIdIn(bookIds);

        // BookDetailDTO로 변환하여 반환
        return bookDetails.stream().map(bookDetail -> {
            ShoppingCart cartItem = cartItems.stream()
                                             .filter(cart -> cart.getBookId().equals(bookDetail.getBookId()))
                                             .findFirst()
                                             .orElse(null);

            return BookDetailDTO.builder()
                    .cartId(cartItem != null ? cartItem.getId() : null)  // cartId를 BookDetailDTO에 포함
                    .bookId(bookDetail.getBookId())
                    .subcategoryId(bookDetail.getSubcategoryId())
                    .topCategoryId(bookDetail.getTopCategoryId())
                    .price(bookDetail.getPrice())
                    .writer(bookDetail.getWriter())
                    .publisher(bookDetail.getPublisher())
                    .bookIndex(bookDetail.getBookIndex())
                    .cover(bookDetail.getCover())
                    .WritersComment(bookDetail.getWritersComment())
                    .rating(bookDetail.getRating())
                    .title(bookDetail.getTitle())
                    .publicationDate(bookDetail.getPublicationDate())
                    .discountRate(bookDetail.getDiscountRate())
                    .quantity(cartItem != null ? cartItem.getQuantity() : null)  
                    .build();
        }).collect(Collectors.toList());
    }

    // 장바구니 아이템 추가
  @Override
public ShoppingCart addCartItem(ShoppingCart item) {
    ShoppingCart existingItem = cartRepository.findByMemberIdAndBookId(item.getMemberId(), item.getBookId());

    if (existingItem != null) {
        existingItem.setQuantity(existingItem.getQuantity() + item.getQuantity());
        logger.info("기존 장바구니 아이템 수량 증가 - 상품 ID: {}, 기존 수량: {}, 증가 수량: {}", 
                     item.getBookId(), existingItem.getQuantity() - item.getQuantity(), item.getQuantity());
        return cartRepository.save(existingItem);
    }

    if (item.getAddedDate() == null) {
        item.setAddedDate(LocalDateTime.now());
    }
    logger.info("장바구니 아이템 추가 요청 - 상품 ID: {}, 수량: {}", item.getBookId(), item.getQuantity());
    return cartRepository.save(item);
}

    // 장바구니 아이템 수정 (수량 변경 등)
    @Override
    public ShoppingCart updateCartItem(ShoppingCart item) {
        ShoppingCart existingCartItem = cartRepository.findById(item.getId())
                .orElseThrow(() -> new RuntimeException("장바구니 아이템이 존재하지 않습니다."));

        // 수량만 업데이트하고 저장
        existingCartItem.setQuantity(item.getQuantity());
        logger.info("장바구니 아이템 수정 - 상품 ID: {}, 수정된 수량: {}", existingCartItem.getBookId(), existingCartItem.getQuantity());
        return cartRepository.save(existingCartItem);
    }

    // 장바구니 아이템 삭제
    @Override
    @Transactional
    public void deleteCartItem(Long id) {
        if (!cartRepository.existsById(id)) {
            throw new RuntimeException("장바구니 아이템이 존재하지 않습니다.");
        }
        logger.info("장바구니 아이템 삭제 요청 - 아이템 ID: {}", id);
        cartRepository.deleteById(id);
        logger.info("장바구니 아이템 삭제 완료 - 삭제된 아이템 ID: {}", id);
    }

    // 여러 아이템을 장바구니에서 삭제
    @Override
    @Transactional
    public void deleteMultipleCartItems(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            throw new IllegalArgumentException("삭제할 아이디 목록이 비어있습니다.");
        }

        logger.info("장바구니 아이템 여러 개 삭제 요청 - 아이템 수: {}", ids.size());
        logger.info("삭제할 아이템 ID 목록: {}", ids);

        // 존재하는 아이디만 필터링하여 삭제
        List<Long> existingIds = ids.stream()
            .filter(cartRepository::existsById)
            .collect(Collectors.toList());

        if (existingIds.isEmpty()) {
            logger.warn("삭제할 아이템이 존재하지 않습니다.");
            throw new RuntimeException("삭제할 장바구니 아이템이 존재하지 않습니다.");
        }

        logger.info("삭제할 아이템 존재 - 아이템 수: {}", existingIds.size());

        cartRepository.deleteAllByIdIn(existingIds);

        existingIds.forEach(id -> {
            if (!cartRepository.existsById(id)) {
                logger.info("아이템 {} 삭제 완료", id);
            } else {
                logger.warn("아이템 {} 삭제 실패", id);
            }
        });
    }

    // 특정 회원의 장바구니 전체 삭제
    @Override
    @Transactional
    public void clearCart(Long memberId) {
        List<ShoppingCart> cartItems = cartRepository.findByMemberId(memberId);
        if (!cartItems.isEmpty()) {
            logger.info("회원 ID {}의 장바구니 전체 삭제 요청 - 아이템 수: {}", memberId, cartItems.size());
            cartRepository.deleteAll(cartItems);
            logger.info("회원 ID {}의 장바구니 전체 삭제 완료", memberId);
        } else {
            logger.info("회원 ID {}의 장바구니에 삭제할 아이템이 없습니다.", memberId);
        }
    }

    // 여러 아이템을 장바구니에 추가
@Override
public void addMultipleCartItems(Long memberId, List<Long> productIds) {
    logger.info("여러 아이템을 장바구니에 추가 요청 - 아이템 수: {}", productIds.size());

    for (Long productId : productIds) {
        ShoppingCart existingItem = cartRepository.findByMemberIdAndBookId(memberId, productId);
        if (existingItem != null) {
            existingItem.setQuantity(existingItem.getQuantity() + 1);
            cartRepository.save(existingItem);
            logger.info("기존 아이템 수량 증가 - 회원 ID: {}, 상품 ID: {}, 새 수량: {}", memberId, productId, existingItem.getQuantity());
        } else {
            ShoppingCart newItem = new ShoppingCart();
            newItem.setMemberId(memberId);
            newItem.setBookId(productId);
            newItem.setQuantity(1);
            newItem.setAddedDate(LocalDateTime.now());
            cartRepository.save(newItem);
            logger.info("신규 아이템 추가 - 회원 ID: {}, 상품 ID: {}", memberId, productId);
        }
    }

    logger.info("여러 아이템이 장바구니에 처리 완료 - 요청된 아이템 수: {}", productIds.size());
}

}
