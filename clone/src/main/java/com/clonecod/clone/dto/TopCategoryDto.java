package com.clonecod.clone.dto;

import com.clonecod.clone.entity.TopCategory;
import lombok.*;

import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TopCategoryDto {
    private int id;
    private String name;
    private List<SubCategoryDto> subCategories;

    public static TopCategoryDto fromEntity(TopCategory topCategory) {
        return TopCategoryDto.builder()
                .id(topCategory.getId())
                .name(topCategory.getName())
                .subCategories(topCategory.getSubCategories().stream()
                        .map(SubCategoryDto::fromEntity)
                        .collect(Collectors.toList()))
                .build();
    }
}