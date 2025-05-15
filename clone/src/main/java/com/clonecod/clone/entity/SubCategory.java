package com.clonecod.clone.entity;

import lombok.*;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "copyoftop_category")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@IdClass(SubCategory.SubCategoryId.class)
public class SubCategory {
    
    @Id
    @Column(name = "Subcategory_Id")
    private Integer id;

    @Id
    @Column(name = "Top_Category_Id")
    private Integer topCategoryId;

    @Column(name = "Sub_Category_name", nullable = false)
    private String name;

    @ManyToOne
    @JoinColumn(name = "Top_Category_Id", insertable = false, updatable = false)
    private TopCategory topCategory;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SubCategoryId implements Serializable {
        private Integer id;
        private Integer topCategoryId;
        
        // equals와 hashCode는 필수
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            SubCategoryId that = (SubCategoryId) o;
            return Objects.equals(id, that.id) && 
                   Objects.equals(topCategoryId, that.topCategoryId);
        }

        @Override
        public int hashCode() {
            return Objects.hash(id, topCategoryId);
        }
    }
}