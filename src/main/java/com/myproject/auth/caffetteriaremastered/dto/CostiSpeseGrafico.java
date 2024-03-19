package com.myproject.auth.caffetteriaremastered.dto;

import lombok.Data;

@Data
public class CostiSpeseGrafico {
    private Long idOrdine;
    private Long idProdotto;
    private double costoTotale;
    private double speseNette;
    private double guadagni;

    public CostiSpeseGrafico(Long idOrdine, Long idProdotto, double costoTotale, double speseNette, double guadagni) {
        this.idOrdine = idOrdine;
        this.idProdotto = idProdotto;
        this.costoTotale = costoTotale;
        this.speseNette = speseNette;
        this.guadagni = guadagni;
    }
}

