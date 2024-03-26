package com.myproject.auth.caffetteriaremastered.dto;


import lombok.Data;

@Data
public class FilterRequestOrdini {
    private Long idCliente;
    private Long idUtente;
    private Long idProdotto;
    private String sortBy;
    private String sortOrder;
    private Integer year;
    private int page;
    private int size;
}
