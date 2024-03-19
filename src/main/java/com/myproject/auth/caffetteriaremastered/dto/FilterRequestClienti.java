package com.myproject.auth.caffetteriaremastered.dto;

import lombok.Data;

@Data
public class FilterRequestClienti {
    private String type;
    private Integer month;
    private Integer year;
    private String genere;
    private String initial;
    private String emailDomain;
    private String sortBy;
    private String sortOrder;
    private int page;
    private int size;
}
