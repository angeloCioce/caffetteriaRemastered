package com.myproject.auth.caffetteriaremastered.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class FilterRequestOrdine {
    private LocalDateTime date;
    private Double price;
    private Integer totalSell;
    private Long userId;
    private int page;
    private int size;
    private String dataOrdine;
    private String prezzoTotale;
    private String quantitaOrdine;
    private String sortBy;
    private  String sortOrder;
}
