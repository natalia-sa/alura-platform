package com.alura.platform.business.basic;

public record PaginationDto(int page, int size) {

    public int getOffset() {
        return (page - 1) * size;
    }
}
