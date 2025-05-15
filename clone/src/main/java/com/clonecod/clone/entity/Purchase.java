package com.clonecod.clone.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "purchase")
public class Purchase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "purchase_id")
    private Long purchaseId;

    @Column(name = "cart_id", nullable = false)
    private Long cartId;

    @Column(name = "book_id", nullable = false)
    private Long bookId;

    @Column(name = "member_id", nullable = false)
    private Long memberId;

    @Column(name = "purchase_date", nullable = false)
    private LocalDateTime purchaseDate;

    @Column(name = "delivery_date")
    private LocalDateTime deliveryDate;  // 🚚 배송날짜 필드 추가

    @Column(nullable = false)
    private int quantity;

    @Builder
    public Purchase(Long orderId, Long cartId, Long bookId, Long memberId, LocalDateTime purchaseDate, LocalDateTime deliveryDate, int quantity) {
        this.purchaseId = purchaseId;
        this.cartId = cartId;
        this.bookId = bookId;
        this.memberId = memberId;
        this.purchaseDate = purchaseDate;
        this.deliveryDate = deliveryDate;  // 🚚 빌더에도 추가
        this.quantity = quantity;
    }
}
