package com.myproject.auth.caffetteriaremastered.dto;

import lombok.Data;

@Data
public class GuadagnoPerditaTotale {
    private double guadagnoTotale;
    private double perditaTotale;

    public GuadagnoPerditaTotale(double guadagnoTotale, double perditaTotale) {
        this.guadagnoTotale = guadagnoTotale;
        this.perditaTotale = perditaTotale;
    }
}
