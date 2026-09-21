package com.ordriva.common.api;

import java.util.List;

public record ApiResponse<T>(T data, Pagination pagination) {
    public static <T> ApiResponse<T> of(T data) {
        return new ApiResponse<>(data, null);
    }

    public static <T> ApiResponse<List<T>> page(List<T> data, int page, int size, long totalElements) {
        return new ApiResponse<>(data, new Pagination(page, size, totalElements, (int) Math.ceil((double) totalElements / size)));
    }
}