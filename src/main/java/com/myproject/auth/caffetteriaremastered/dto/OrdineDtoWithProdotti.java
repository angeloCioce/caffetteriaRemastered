package com.myproject.auth.caffetteriaremastered.dto;

import lombok.Data;

import javax.swing.*;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class OrdineDtoWithProdotti {
    private Long id_ordine;
    private LocalDateTime data_ordine;
    private Double prezzo_totale;
    private Long id_cliente;
    private Long id_utente;
    private List<Long> prodottoOrdiniId;
}
