package com.myproject.auth.caffetteriaremastered.dto;


import lombok.Data;

import java.util.List;

@Data
public class ProdottoDtoWIthCategorie {
    private Long id_prodotto;
    private String nome_prodotto;
    private String descrizione;
    private Double prezzo_ingrosso;
    private Double prezzo_dettaglio;
    private Integer quantita;
    private List<String> categorie;
}
