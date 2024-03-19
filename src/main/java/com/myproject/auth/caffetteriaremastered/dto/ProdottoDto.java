package com.myproject.auth.caffetteriaremastered.dto;

import lombok.Data;

import java.util.List;

@Data
public class ProdottoDto {
    private Long id_prodotto;
    private String nome_prodotto;
    private String descrizione;
    private Double prezzoIngrosso;
    private Double prezzoDettaglio;
    private Integer quantita;
    private List<Long> id_categoria;
    private List<CategoriaDto> categoria;
    private List<String> categorie;
    private int page;
    private int size;
}
