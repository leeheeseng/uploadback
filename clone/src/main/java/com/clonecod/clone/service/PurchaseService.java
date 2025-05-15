package com.clonecod.clone.service;

import com.clonecod.clone.dto.PurchaseDto;

import java.time.LocalDateTime;
import java.util.List;

public interface PurchaseService {
    List<PurchaseDto> savePurchases(List<PurchaseDto> dtoList);
        List<PurchaseDto> findAllPurchases();
    List<PurchaseDto> findPurchasesByMemberId(Long memberId);  // <-- 추가
    boolean deletePurchasesByIdsAndMemberId(List<Long> purchaseIds, Long memberId);
}
