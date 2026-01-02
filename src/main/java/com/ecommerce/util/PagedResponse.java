package com.ecommerce.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Generic paged response wrapper for paginated API responses.
 * Provides metadata about pagination along with the data.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PagedResponse<T> {

    private List<T> content;
    private int page;
    private int size;
    private long totalElements;
    private int totalPages;
    private boolean last;
    private boolean first;
    private boolean empty;

    /**
     * Create a paged response from the given parameters.
     *
     * @param content the list of items
     * @param page the current page number
     * @param size the page size
     * @param totalElements the total number of elements
     * @param <T> the type of the content items
     * @return PagedResponse object
     */
    public static <T> PagedResponse<T> of(List<T> content, int page, int size, long totalElements) {
        int totalPages = size > 0 ? (int) Math.ceil((double) totalElements / size) : 0;
        
        return PagedResponse.<T>builder()
                .content(content)
                .page(page)
                .size(size)
                .totalElements(totalElements)
                .totalPages(totalPages)
                .last(page >= totalPages - 1)
                .first(page == 0)
                .empty(content.isEmpty())
                .build();
    }

    /**
     * Create a paged response with all pagination metadata.
     *
     * @param content the list of items
     * @param page the current page number
     * @param size the page size
     * @param totalElements the total number of elements
     * @param totalPages the total number of pages
     * @param last whether this is the last page
     * @param first whether this is the first page
     * @param <T> the type of the content items
     * @return PagedResponse object
     */
    public static <T> PagedResponse<T> of(List<T> content, int page, int size, 
                                           long totalElements, int totalPages, 
                                           boolean last, boolean first) {
        return PagedResponse.<T>builder()
                .content(content)
                .page(page)
                .size(size)
                .totalElements(totalElements)
                .totalPages(totalPages)
                .last(last)
                .first(first)
                .empty(content.isEmpty())
                .build();
    }

    /**
     * Create an empty paged response.
     *
     * @param <T> the type of the content items
     * @return an empty PagedResponse object
     */
    public static <T> PagedResponse<T> empty() {
        return PagedResponse.<T>builder()
                .content(List.of())
                .page(0)
                .size(0)
                .totalElements(0)
                .totalPages(0)
                .last(true)
                .first(true)
                .empty(true)
                .build();
    }
}
