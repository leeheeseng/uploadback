package com.clonecod.clone.controller;

import com.clonecod.clone.dto.PurchaseDeleteRequest;
import com.clonecod.clone.dto.PurchaseDto;
import com.clonecod.clone.service.PurchaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/purchases")
public class PurchaseController {

    private final PurchaseService purchaseService;

    @PostMapping
    public ResponseEntity<List<PurchaseDto>> createPurchase(@RequestBody List<PurchaseDto> dtoList) {
        List<PurchaseDto> saved = purchaseService.savePurchases(dtoList);  // 여러 개의 구매 등록
        return ResponseEntity.ok(saved);
    }

    // 전체 구매 목록 조회 - 관리자용 or 테스트용
    @GetMapping
    public ResponseEntity<List<PurchaseDto>> getAllPurchases() {
        return ResponseEntity.ok(purchaseService.findAllPurchases());
    }

    // 특정 회원의 구매 목록 조회 - 마이페이지에서 사용
    @GetMapping("/{memberId}")
    public ResponseEntity<List<PurchaseDto>> getPurchasesByMemberId(@PathVariable Long memberId) {
        List<PurchaseDto> purchases = purchaseService.findPurchasesByMemberId(memberId);
        return ResponseEntity.ok(purchases);
    }
    @DeleteMapping("/delete")
    public ResponseEntity<String> deletePurchases(@RequestBody PurchaseDeleteRequest request) {
        System.out.println("Received data: " + request);  // 로깅
        System.out.println("purchaseIds: " + request.getPurchaseIds());
System.out.println("memberId: " + request.getMemberId());
        try {
            boolean isDeleted = purchaseService.deletePurchasesByIdsAndMemberId(request.getPurchaseIds(), request.getMemberId());
            if (isDeleted) {
                return ResponseEntity.ok("주문이 삭제되었습니다.");
            } else {
                return ResponseEntity.status(404).body("주문을 찾을 수 없습니다.");
            }
        } catch (Exception e) {
            return ResponseEntity.status(500).body("서버 오류가 발생했습니다.");
        }
    }
    
      
}
