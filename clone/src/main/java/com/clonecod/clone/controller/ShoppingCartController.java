package com.clonecod.clone.controller;

import com.clonecod.clone.dto.AddToCartRequest;
import com.clonecod.clone.dto.BookDetailDTO;
import com.clonecod.clone.entity.BookDetail;
import com.clonecod.clone.entity.ShoppingCart;
import com.clonecod.clone.service.ShoppingCartService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class ShoppingCartController {

    private final ShoppingCartService cartService;
    private static final Logger logger = LoggerFactory.getLogger(ShoppingCartController.class);

    // 장바구니 안의 책 목록 조회 (회원별)
    @GetMapping("/{memberId}/books")
    public List<BookDetailDTO> getBooksInCart(@PathVariable Long memberId) {
        return cartService.getBooksInCart(memberId);

    }

    // 장바구니 아이템 추가
    @PostMapping
    public ShoppingCart addCartItem(@RequestBody ShoppingCart cart) {
        logger.info("장바구니 아이템 추가 요청 - 상품ID: {}, 수량: {}", cart.getBookId(), cart.getQuantity());
        ShoppingCart addedCart = cartService.addCartItem(cart);
        logger.info("장바구니 아이템 추가 완료 - 상품ID: {} | 장바구니 ID: {}", addedCart.getBookId(), addedCart.getId());
        return addedCart;
    }

    // 장바구니 아이템 여러 개 추가
    @PostMapping("/batch")
    public String addMultipleCartItems(@RequestBody AddToCartRequest request) {
        logger.info("장바구니 아이템 여러 개 추가 요청 - 회원ID: {}, 상품수: {}", request.getMemberId(), request.getBookIds().size());
        try {
            cartService.addMultipleCartItems(request.getMemberId(), request.getBookIds());
            logger.info("{}개의 상품이 장바구니에 추가되었습니다.", request.getBookIds().size());
        } catch (Exception e) {
            logger.error("장바구니 아이템 여러 개 추가 중 오류 발생 - 회원ID: {}, 오류 메시지: {}", request.getMemberId(), e.getMessage());
            throw e;
        }
        return request.getBookIds().size() + "개 상품이 장바구니에 추가되었습니다.";
    }

    // 장바구니 아이템 수정 (수량 변경 등)
    @PutMapping("/{cartId}")
    public ShoppingCart updateCartItemQuantity(@PathVariable Long cartId, @RequestBody ShoppingCart cart) {
        logger.info("장바구니 아이템 수정 요청 - cartId: {}, 새로운 수량: {}", cartId, cart.getQuantity());
        cart.setId(cartId);  // cartId를 본문 데이터에 반영
        ShoppingCart updatedCart = null;
        try {
            updatedCart = cartService.updateCartItem(cart);
            logger.info("장바구니 아이템 수정 완료 - cartId: {} | 새로운 수량: {}", cartId, updatedCart.getQuantity());
        } catch (Exception e) {
            logger.error("장바구니 아이템 수정 실패 - cartId: {}, 오류 메시지: {}", cartId, e.getMessage());
            throw e;
        }
        return updatedCart;
    }

    // 장바구니 아이템 삭제
    @DeleteMapping("/{id}")
    public void deleteCartItem(@PathVariable Long id) {
        logger.info("장바구니 아이템 삭제 요청 - cartId: {}", id);
        try {
            cartService.deleteCartItem(id);
            logger.info("장바구니 아이템 삭제 완료 - cartId: {}", id);
        } catch (Exception e) {
            logger.error("장바구니 아이템 삭제 실패 - cartId: {}, 오류 메시지: {}", id, e.getMessage());
            throw e;
        }
    }

    // 장바구니 아이템 여러 개 삭제 (배치 삭제)
    @DeleteMapping("/batch")
    public void deleteMultipleCartItems(@RequestBody List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            logger.error("삭제할 아이디 목록이 비어있습니다.");
            throw new IllegalArgumentException("삭제할 아이디 목록이 비어있습니다.");
        }
    
        logger.info("장바구니 아이템 여러 개 삭제 요청 - 삭제할 아이템 수: {}", ids.size());
        logger.info("삭제할 아이템 ID 목록: {}", ids);
        try {
            cartService.deleteMultipleCartItems(ids);  // 여기는 List<Long> ids
            logger.info("장바구니 아이템 여러 개 삭제 완료 - 삭제된 아이템 수: {}", ids.size());
        } catch (Exception e) {
            logger.error("장바구니 아이템 여러 개 삭제 실패 - 오류 메시지: {}", e.getMessage());
            throw e;
        }
    }

    // 장바구니 전체 삭제
    @DeleteMapping("/clear/{memberId}")
    public void clearCart(@PathVariable Long memberId) {
        logger.info("장바구니 전체 삭제 요청 - 회원ID: {}", memberId);
        try {
            cartService.clearCart(memberId);
            logger.info("장바구니 전체 삭제 완료 - 회원ID: {}", memberId);
        } catch (Exception e) {
            logger.error("장바구니 전체 삭제 실패 - 회원ID: {}, 오류 메시지: {}", memberId, e.getMessage());
            throw e;
        }
    }

    // 예외 처리 예시
    @ExceptionHandler(Exception.class)
    public String handleException(Exception ex) {
        logger.error("오류 발생: {}", ex.getMessage());
        return "오류가 발생했습니다: " + ex.getMessage();
    }
}
