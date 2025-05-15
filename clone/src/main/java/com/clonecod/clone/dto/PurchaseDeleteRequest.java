package com.clonecod.clone.dto;

import java.util.List;

public class PurchaseDeleteRequest {
    private List<Long> purchaseIds;  // 삭제할 구매 ID 목록
    private Long memberId;           // 회원 ID

    // Getters and Setters
    public List<Long> getPurchaseIds() {
        return purchaseIds;
    }

    public void setPurchaseIds(List<Long> purchaseIds) {
        this.purchaseIds = purchaseIds;
    }

    public Long getMemberId() {
        return memberId;
    }

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }
}
