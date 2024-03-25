package com.alura.platform.business.basic;

public record PaginationDto(Integer page, Integer size) {

    public int getOffset() {
        return (page - 1) * size;
    }
}
