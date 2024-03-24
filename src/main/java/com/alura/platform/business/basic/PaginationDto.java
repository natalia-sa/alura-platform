package com.alura.platform.business.basic;

import jakarta.validation.constraints.NotNull;

public record PaginationDto(
        @NotNull
        int page,

        @NotNull
        int size) {

    public int getOffset() {
        return (page - 1) * size;
    }
}
