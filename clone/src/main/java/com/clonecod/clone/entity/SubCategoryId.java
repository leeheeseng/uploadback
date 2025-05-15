package com.clonecod.clone.entity;

import java.io.Serializable;
import java.util.Objects;

import jakarta.persistence.Embeddable;
@Embeddable
public class SubCategoryId implements Serializable {
    private int id;
    private int topCategoryId;

    // 기본 생성자, getter, setter, equals, hashCode
    public SubCategoryId() {}

    public SubCategoryId(Integer id, Integer topCategoryId) {
        this.id = id;
        this.topCategoryId = topCategoryId;
    }

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