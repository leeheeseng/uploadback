package com.clonecod.clone.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.time.LocalDateTime;

@Data
@Builder
@Getter
@Setter
public class PurchaseDto {
    private Long purchaseId;
    private Long cartId;
    private Long bookId;  // 추가
    private Long memberId;
    private LocalDateTime purchaseDate;
    private LocalDateTime deliveryDate;  // 🚚 배송일자 추가!
    private int quantity;

    // 기본 생성자
    public PurchaseDto() {
    }

    @JsonCreator
    public PurchaseDto(
            @JsonProperty("purchaseId") Long purchaseId,
            @JsonProperty("cartId") Long cartId,
            @JsonProperty("bookId") Long bookId,  // 추가
            @JsonProperty("memberId") Long memberId,
            @JsonProperty("purchaseDate") LocalDateTime purchaseDate,
            @JsonProperty("deliveryDate") LocalDateTime deliveryDate,  // 🚚 추가
            @JsonProperty("quantity") int quantity) {
        this.purchaseId = purchaseId;
        this.cartId = cartId;
        this.bookId = bookId;
        this.memberId = memberId;
        this.purchaseDate = purchaseDate;
        this.deliveryDate = deliveryDate;  // 🚚 세팅
        this.quantity = quantity;
    }
}
