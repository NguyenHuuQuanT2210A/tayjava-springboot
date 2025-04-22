package com.example.backendservice.controller.response;

import lombok.*;

@Getter
@Setter
public abstract class PageResponseAbstract {
    private int pageNumber;
    private int pageSize;
    private long totalElements;
    private int totalPages;
}
