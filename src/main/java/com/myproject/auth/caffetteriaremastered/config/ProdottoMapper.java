package com.myproject.auth.caffetteriaremastered.config;

import com.myproject.auth.caffetteriaremastered.dto.ProdottoDto;
import com.myproject.auth.caffetteriaremastered.model.Prodotto;
import org.springframework.beans.BeanUtils;

public class ProdottoMapper {

    public static ProdottoDto mapToProdottoDto(Prodotto prodotto) {
        ProdottoDto prodottoDto = new ProdottoDto();
        BeanUtils.copyProperties(prodotto, prodottoDto);


        return prodottoDto;
    }
}
