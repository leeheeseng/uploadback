package com.clonecod.clone.repository;

import com.clonecod.clone.entity.ShoppingCart;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ShoppingCartRepository extends JpaRepository<ShoppingCart, Long> {
    
    // 특정 회원의 장바구니 아이템 조회
    List<ShoppingCart> findByMemberId(Long memberId);

    // 특정 ID 목록에 해당하는 장바구니 아이템 삭제 (배치 삭제)
    void deleteAllByIdIn(List<Long> existingIds);


    // 특정 회원의 모든 장바구니 아이템 삭제 (회원별 장바구니 비우기)
    void deleteAllByMemberId(Long memberId);

    ShoppingCart findByMemberIdAndBookId(Long memberId, Long bookId);
}
