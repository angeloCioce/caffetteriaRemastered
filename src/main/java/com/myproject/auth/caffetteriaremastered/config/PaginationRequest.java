package com.myproject.auth.caffetteriaremastered.config;

import lombok.Data;

@Data
public class PaginationRequest {
    private int year;
    private int month;
    private int page;
    private int size;
}
