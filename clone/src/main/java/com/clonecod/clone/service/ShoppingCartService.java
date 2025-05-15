package com.clonecod.clone.service;

import com.clonecod.clone.dto.BookDetailDTO;
import com.clonecod.clone.entity.BookDetail;
import com.clonecod.clone.entity.ShoppingCart;

import java.util.List;

public interface ShoppingCartService {

    // 특정 회원의 장바구니 아이템 조회
   List<BookDetailDTO> getBooksInCart(Long memberId);

    // 장바구니 아이템 추가
    ShoppingCart addCartItem(ShoppingCart item);

    // 장바구니 아이템 수정
    ShoppingCart updateCartItem(ShoppingCart item);

    // 장바구니 아이템 삭제
    void deleteCartItem(Long id);

    // 여러 장바구니 아이템 삭제 (배치 삭제)
    void deleteMultipleCartItems(List<Long> ids);
    void addMultipleCartItems(Long memberId, List<Long> productIds);
    // 특정 회원의 모든 장바구니 아이템 삭제 (장바구니 비우기)
    void clearCart(Long memberId);
}
