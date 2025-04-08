package com.example.backendservice.controller.response;

import lombok.*;

@Getter
@Setter
public abstract class PageResponseAbstract {
    public int pageNumber;
    public int pageSize;
    public long totalElements;
    public int totalPages;
}
