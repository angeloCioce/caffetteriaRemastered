package com.myproject.auth.caffetteriaremastered.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ProdottoPercentualeVendite {
    private Long idProdotto;
    private double percentualeVendite;
}

