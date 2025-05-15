package com.clonecod.clone.dto;

public class CartItemDto {
    private Long cartId;
    private Long memberId;
    private Long bookId;
    private Integer quantity;

    // 기본 생성자
    public CartItemDto() {}

    // 전체 생성자
    public CartItemDto(Long cartId, Long memberId, Long bookId, Integer quantity) {
        this.cartId = cartId;
        this.memberId = memberId;
        this.bookId = bookId;
        this.quantity = quantity;
    }

    // Getter, Setter
    public Long getCartId() {
        return cartId;
    }

    public void setCartId(Long cartId) {
        this.cartId = cartId;
    }

    public Long getMemberId() {
        return memberId;
    }

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }

    public Long getBookId() {
        return bookId;
    }

    public void setBookId(Long bookId) {
        this.bookId = bookId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}
