package com.clonecod.clone.repository;

import com.clonecod.clone.entity.SubCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SubCategoryRepository extends JpaRepository<SubCategory, SubCategory.SubCategoryId> {

    // 잘못된 방식 (복합키 내부에 직접 접근하려고 시도함)
    // List<SubCategory> findById_TopCategoryId(Integer topCategoryId); ❌ 제거!

    // ✅ 올바른 방식 1: 직접 필드 명을 사용한 메서드
    List<SubCategory> findByTopCategoryId(Integer topCategoryId);

    // ✅ 올바른 방식 2: JPQL로 명시적으로 지정
    @Query("SELECT s FROM SubCategory s WHERE s.topCategoryId = :topCategoryId")
    List<SubCategory> findByTopCategoryIdWithQuery(@Param("topCategoryId") Integer topCategoryId);
}
