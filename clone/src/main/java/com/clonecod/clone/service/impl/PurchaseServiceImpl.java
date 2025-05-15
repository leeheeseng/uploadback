package com.clonecod.clone.service.impl;

import com.clonecod.clone.dto.PurchaseDto;
import com.clonecod.clone.entity.Purchase;
import com.clonecod.clone.repository.PurchaseRepository;
import com.clonecod.clone.service.PurchaseService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PurchaseServiceImpl implements PurchaseService {

    private final PurchaseRepository purchaseRepository;

    @Override
    public List<PurchaseDto> savePurchases(List<PurchaseDto> dtoList) {
        List<Purchase> purchases = dtoList.stream()
                .map(dto -> Purchase.builder()
                        .memberId(dto.getMemberId())
                        .cartId(dto.getCartId())
                        .bookId(dto.getBookId())
                        .quantity(dto.getQuantity())
                        .purchaseDate(LocalDateTime.now())    // 구매일자
                        .deliveryDate(LocalDateTime.now().plusDays(3))  // 배송일자 3일 후로 기본 설정
                        .build())
                .collect(Collectors.toList());

        List<Purchase> savedPurchases = purchaseRepository.saveAll(purchases);

        return savedPurchases.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<PurchaseDto> findAllPurchases() {
        return purchaseRepository.findAll().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<PurchaseDto> findPurchasesByMemberId(Long memberId) {
        return purchaseRepository.findByMemberId(memberId).stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    private PurchaseDto toDto(Purchase entity) {
        return PurchaseDto.builder()
                .purchaseId(entity.getPurchaseId())       
                .memberId(entity.getMemberId())     
                .cartId(entity.getCartId())         
                .bookId(entity.getBookId())         
                .quantity(entity.getQuantity())     
                .purchaseDate(entity.getPurchaseDate())  
                .deliveryDate(entity.getDeliveryDate())  // 🧡 배송일자 추가
                .build();
    }

    @Override
    @Transactional
    public boolean deletePurchasesByIdsAndMemberId(List<Long> purchaseIds, Long memberId) {
        try {
            int deletedCount = purchaseRepository.deleteByPurchaseIdInAndMemberId(purchaseIds, memberId);
            return deletedCount > 0;
        } catch (Exception e) {
            throw new RuntimeException("주문 삭제 실패: " + e.getMessage(), e);
        }
    }
    
    
    

    
    
}
