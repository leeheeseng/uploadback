package com.clonecod.clone.entity;

import lombok.*;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "top_category")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TopCategory {
    @Id
    @Column(name = "Top_Category_Id")
    private int id;

    @Column(name = "Top_Category_name", nullable = false)
    private String name;

    @OneToMany(mappedBy = "topCategory")
    private List<SubCategory> subCategories;
}