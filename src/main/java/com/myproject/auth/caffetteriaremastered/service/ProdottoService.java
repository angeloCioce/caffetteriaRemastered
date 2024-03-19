package com.myproject.auth.caffetteriaremastered.service;

import com.myproject.auth.caffetteriaremastered.dto.ProdottoDto;
import com.myproject.auth.caffetteriaremastered.dto.ProdottoDtoWIthCategorie;
import com.myproject.auth.caffetteriaremastered.model.Prodotto;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;

public interface ProdottoService {

    Prodotto save(ProdottoDto prodottoDto);

    ProdottoDtoWIthCategorie findById(Long id);

    void delete(Long id);

    Prodotto update(Long id, ProdottoDto prodottoDto, List<Long> idCategorie);

    Page<Prodotto> getProdottoByCategoria(String categoria, int page, int size);

    Page<Prodotto> getProdottoByInitial(String initial, int page, int size);

    Page<Prodotto> applyFilters(Map<String, Object> filters, int page, int size, String sortBy, String sortOrder);
}
