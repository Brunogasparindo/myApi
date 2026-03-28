package com.example.demo.interfaces.rest;

import java.util.List;

public record PageResponse<T>(
        List<T> content,
        long totalElements,
        int totalPages,
        int currentPage,
        int pageSize,
        boolean hasMore
) {}
