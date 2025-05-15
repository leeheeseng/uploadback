package com.clonecod.clone.dto;

import com.clonecod.clone.entity.SubCategory;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SubCategoryDto {
    private int id;
    private String name;
    private int topCategoryId;

    public static SubCategoryDto fromEntity(SubCategory subCategory) {
        return SubCategoryDto.builder()
                .id(subCategory.getId())
                .name(subCategory.getName())
                .topCategoryId(subCategory.getTopCategoryId())
                .build();
    }
}