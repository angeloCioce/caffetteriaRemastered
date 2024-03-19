package com.myproject.auth.caffetteriaremastered.controller;

import com.myproject.auth.caffetteriaremastered.dto.*;
import com.myproject.auth.caffetteriaremastered.model.Prodotto;
import com.myproject.auth.caffetteriaremastered.service.ProdottoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/prodotto")
public class ProdottoController {

    @Autowired
    private ProdottoService prodottoService;

    @PreAuthorize("hasAnyAuthority('ADMIN', 'MANUTENTORE', 'DIPENDENTE')")
    @PostMapping("/addProdotto")
    public ResponseEntity<ProdottoDto> saveNewProdotto(@RequestBody ProdottoDto prodottoDto)
    {
        Prodotto prodotto = prodottoService.save(prodottoDto);
        return new ResponseEntity<>(prodottoDto, HttpStatus.CREATED);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'MANUTENTORE', 'DIPENDENTE')")
    @GetMapping("/{id}")
    public ResponseEntity<ProdottoDtoWIthCategorie> getProdottoWithCategorieById(@PathVariable("id") Long id) {
        ProdottoDtoWIthCategorie prodottoDtoWIthCategorie = prodottoService.findById(id);
        return new ResponseEntity<>(prodottoDtoWIthCategorie, HttpStatus.OK);
    }


    @GetMapping("/byCategoria")
    public ResponseEntity<Page<Prodotto>> getProdottoByCategoria(
            @RequestParam("categoria") String categoria,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<Prodotto> prodotti = prodottoService.getProdottoByCategoria(categoria, page, size);
        return new ResponseEntity<>(prodotti, HttpStatus.OK);
    }

    @GetMapping("/byInitial")
    public ResponseEntity<Page<Prodotto>> getProdottoByInitial(
            @RequestParam("initial") String initial,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<Prodotto> prodotti = prodottoService.getProdottoByInitial(initial, page, size);
        return new ResponseEntity<>(prodotti, HttpStatus.OK);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'DIPENDENTE', 'MANUTENTORE')")
    @PostMapping("/prodotti/filtri.dinamici")
    public ResponseEntity<?> filterProdottiDinamici(@Validated @RequestBody FilterRequestProdotto filterRequest) {
        Map<String, Object> filters = new HashMap<>();

        if (filterRequest.getCategoria() != null) {
            filters.put("categoria", filterRequest.getCategoria());
        }

        if (filterRequest.getInitial() != null) {
            filters.put("initial", filterRequest.getInitial());
        }

        if (filterRequest.getPrezzoIngrosso() != null) {
            filters.put("prezzoIngrosso", filterRequest.getPrezzoIngrosso());
        }

        if (filterRequest.getPrezzoDettaglio() != null) {
            filters.put("prezzoDettaglio", filterRequest.getPrezzoDettaglio());
        }

        if (filterRequest.getQuantita() != null) {
            filters.put("quantita", filterRequest.getQuantita());
        }

        String sortBy = filterRequest.getSortBy();
        String sortOrder = filterRequest.getSortOrder();

        if (sortBy != null && sortOrder != null) {
            filters.put("sortBy", sortBy);
            filters.put("sortOrder", sortOrder);
        }

        int page = filterRequest.getPage();
        int size = filterRequest.getSize();

        Page<Prodotto> filteredResults = prodottoService.applyFilters(filters, page, size, sortBy, sortOrder);
        return new ResponseEntity<>(filteredResults, HttpStatus.OK);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'DIPENDENTE', 'MANUTENTORE')")
    @PatchMapping("/update/{id_prodotto}")
    public ResponseEntity<Prodotto> patchProdotto(@PathVariable("id_prodotto") Long id,
                                                  @Validated @RequestBody ProdottoUpdateRequest request)
    {
        Prodotto prodottoAggiornato = prodottoService.update(id, request.getProdottoDto(), request.getIdCategorie());
        return ResponseEntity.ok().body(prodottoAggiornato);
    }

}
