package com.myproject.auth.caffetteriaremastered.dto;

import lombok.Data;

import java.util.List;

@Data
public class ProdottoUpdateRequest {
    private ProdottoDto prodottoDto;
    private List<Long> idCategorie;
}
