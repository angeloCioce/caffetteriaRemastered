package com.myproject.auth.caffetteriaremastered.dto;

import lombok.Data;

@Data
public class FilterRequestProdotto {
    private String type;
    private Long categoriaId;
    private String initial;
    private String sortBy;
    private String sortOrder;
    private String prezzoDettaglio;
    private String prezzoIngrosso;
    private String quantita;
    private int page;
    private int size;
}
