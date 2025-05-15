package com.clonecod.clone.dto;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

public class PagedResponse<T> {
    private final List<T> content;
    private final long totalElements;
    private final int totalPages;
    private final int currentPage;
    private final int pageSize;

    private PagedResponse(List<T> content, long totalElements, int totalPages, int currentPage, int pageSize) {
        // 유효성 검사
        Objects.requireNonNull(content, "Content cannot be null");
        if (totalElements < 0) {
            throw new IllegalArgumentException("Total elements cannot be negative");
        }
        if (totalPages < 0) {
            throw new IllegalArgumentException("Total pages cannot be negative");
        }
        if (currentPage < 1) {
            throw new IllegalArgumentException("Current page must be greater than 0");
        }
        if (pageSize < 1) {
            throw new IllegalArgumentException("Page size must be greater than 0");
        }
        if (!content.isEmpty() && content.size() > pageSize) {
            throw new IllegalArgumentException("Content size cannot exceed page size");
        }
        if (totalPages > 0 && currentPage > totalPages) {
            throw new IllegalArgumentException("Current page cannot be greater than total pages");
        }

        this.content = Collections.unmodifiableList(content);
        this.totalElements = totalElements;
        this.totalPages = totalPages;
        this.currentPage = currentPage;
        this.pageSize = pageSize;
    }

    // 빌더 패턴
    public static <T> Builder<T> builder() {
        return new Builder<>();
    }

    public static class Builder<T> {
        private List<T> content = Collections.emptyList();
        private long totalElements;
        private int totalPages;
        private int currentPage = 1;
        private int pageSize = 20;

        public Builder<T> content(List<? extends T> content) {
            this.content = Collections.unmodifiableList(new ArrayList<>(Objects.requireNonNull(content)));
            return this;
        }

        public Builder<T> totalElements(long totalElements) {
            if (totalElements < 0) {
                throw new IllegalArgumentException("Total elements cannot be negative");
            }
            this.totalElements = totalElements;
            return this;
        }

        public Builder<T> totalPages(int totalPages) {
            if (totalPages < 0) {
                throw new IllegalArgumentException("Total pages cannot be negative");
            }
            this.totalPages = totalPages;
            return this;
        }

        public Builder<T> currentPage(int currentPage) {
            if (currentPage < 1) {
                throw new IllegalArgumentException("Current page must be greater than 0");
            }
            this.currentPage = currentPage;
            return this;
        }

        public Builder<T> pageSize(int pageSize) {
            if (pageSize < 1) {
                throw new IllegalArgumentException("Page size must be greater than 0");
            }
            this.pageSize = pageSize;
            return this;
        }

        public PagedResponse<T> build() {
            // 추가적인 일관성 검사
            if (!content.isEmpty() && content.size() > pageSize) {
                throw new IllegalStateException("Content size cannot exceed page size");
            }
            if (totalPages > 0 && currentPage > totalPages) {
                throw new IllegalStateException("Current page cannot be greater than total pages");
            }
            return new PagedResponse<>(content, totalElements, totalPages, currentPage, pageSize);
        }
    }

    // Getters
    public List<T> getContent() {
        return content;
    }

    public long getTotalElements() {
        return totalElements;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public int getPageSize() {
        return pageSize;
    }

    // 편의 메서드
    public boolean hasNext() {
        return currentPage < totalPages;
    }

    public boolean hasPrevious() {
        return currentPage > 1;
    }

    public boolean isEmpty() {
        return content.isEmpty();
    }

    // 정적 팩토리 메서드
    public static <T> PagedResponse<T> of(List<T> content, long totalElements, int totalPages, int currentPage, int pageSize) {
        return (PagedResponse<T>) builder()
            .content(content)
            .totalElements(totalElements)
            .totalPages(totalPages)
            .currentPage(currentPage)
            .pageSize(pageSize)
            .build();
    }

    // Spring Data Page 변환
    public static <T> PagedResponse<T> fromPage(org.springframework.data.domain.Page<T> page) {
        Objects.requireNonNull(page, "Page cannot be null");
        return new PagedResponse<>(
            page.getContent(),
            page.getTotalElements(),
            page.getTotalPages(),
            page.getNumber() + 1, // Spring Data Page는 0-based
            page.getSize()
        );
    }

    // equals, hashCode, toString
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PagedResponse<?> that = (PagedResponse<?>) o;
        return totalElements == that.totalElements &&
               totalPages == that.totalPages &&
               currentPage == that.currentPage &&
               pageSize == that.pageSize &&
               content.equals(that.content);
    }

    @Override
    public int hashCode() {
        return Objects.hash(content, totalElements, totalPages, currentPage, pageSize);
    }

    @Override
    public String toString() {
        return "PagedResponse{" +
               "contentSize=" + content.size() +
               ", totalElements=" + totalElements +
               ", totalPages=" + totalPages +
               ", currentPage=" + currentPage +
               ", pageSize=" + pageSize +
               ", hasNext=" + hasNext() +
               ", hasPrevious=" + hasPrevious() +
               '}';
    }
      // 1. withError 메서드 추가
    public static <T> PagedResponse<T> withError(String errorMessage, int currentPage, int pageSize) {
        return new PagedResponse<>(Collections.emptyList(), 0, 0, currentPage, pageSize);
    }

    // 2. toPageable 메서드 추가
    public Pageable toPageable() {
        return PageRequest.of(currentPage - 1, pageSize);
    }

    // 3. 예시로 추가할 수 있는 다른 메서드들

    // 이전 페이지 번호 가져오기
    public int getPreviousPage() {
        return hasPrevious() ? currentPage - 1 : 1;
    }

    // 다음 페이지 번호 가져오기
    public int getNextPage() {
        return hasNext() ? currentPage + 1 : totalPages;
    }

    
}