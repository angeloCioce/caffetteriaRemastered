package com.myproject.auth.caffetteriaremastered.dto;

import com.myproject.auth.caffetteriaremastered.model.Utente;
import lombok.Data;

@Data
public class UtentePercentualeVendite {
    private Long userId;
    private double percentualeVendite;

    public UtentePercentualeVendite(Long userId, double percentualeVendite) {
        this.userId = userId;
        this.percentualeVendite = percentualeVendite;
    }
}
