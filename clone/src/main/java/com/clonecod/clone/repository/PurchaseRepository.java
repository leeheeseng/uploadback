package com.clonecod.clone.repository;

import com.clonecod.clone.entity.Purchase;

import java.time.LocalDateTime;
import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface PurchaseRepository extends JpaRepository<Purchase, Long> {
    List<Purchase> findByMemberId(Long memberId);

    int deleteByPurchaseIdInAndMemberId(List<Long> purchaseIds, Long memberId);

    
}
