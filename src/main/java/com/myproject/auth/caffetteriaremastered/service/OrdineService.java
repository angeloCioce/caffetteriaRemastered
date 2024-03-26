package com.myproject.auth.caffetteriaremastered.service;

import com.myproject.auth.caffetteriaremastered.dto.*;
import com.myproject.auth.caffetteriaremastered.model.Ordine;
import com.myproject.auth.caffetteriaremastered.model.Prodotti_Ordini;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

public interface OrdineService {

    Ordine save(OrdineDto ordineDto);

    OrdineDto findById(Long id);

    void delete(Long id);

    Ordine update(Long id, OrdineDto ordineDto, List<IdProdottoQuantita> idProdotti);

    List<ProdottoPercentualeVendite> calcolaPercentualeProdottiPiuVenduti(Long idOrdine);

    List<ProdottoPercentualeVendite> calcolaPercentualeProdottiSuTuttiOrdini();

    List<CostiSpeseGrafico> calcolaCostiSpeseGrafico();

    List<ClientePercentualeOrdini> getClientiPercentualeAcquistoAnno(int anno);

    GuadagnoPerditaTotale calcolaGuadagnoPerditaTotale();

    List<UtentePercentualeVendite> getUtentiPercentualeVendite();

    Page<?> applyFilters(Map<String, Object> filters, int page, int size, String sortBy, String sortOrder);
}
