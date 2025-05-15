package com.clonecod.clone.repository;

import com.clonecod.clone.entity.TopCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TopCategoryRepository extends JpaRepository<TopCategory, Integer> {
    @Query("SELECT DISTINCT t FROM TopCategory t LEFT JOIN FETCH t.subCategories")
    List<TopCategory> findAllWithSubCategories();
}