package com.myproject.auth.caffetteriaremastered.dto;

import com.myproject.auth.caffetteriaremastered.model.Cliente;
import lombok.Data;

@Data
public class ClientePercentualeOrdini {
    private Long clienteId;
    private double percentualeOrdini;

    public ClientePercentualeOrdini(Long cliente, double percentualeOrdini) {
        this.clienteId = cliente;
        this.percentualeOrdini = percentualeOrdini;
    }
}

