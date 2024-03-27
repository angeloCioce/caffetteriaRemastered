package com.myproject.auth.caffetteriaremastered.controller;

import com.myproject.auth.caffetteriaremastered.dto.FilterRequestOrdini;
import com.myproject.auth.caffetteriaremastered.dto.OrdineDto;
import com.myproject.auth.caffetteriaremastered.dto.OrdineUpdateRequest;
import com.myproject.auth.caffetteriaremastered.model.Ordine;
import com.myproject.auth.caffetteriaremastered.service.OrdineService;
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
@RequestMapping("/ordine")
public class OrdineController {

    @Autowired
    private OrdineService ordineService;


    @PreAuthorize("hasAnyAuthority('ADMIN', 'MANUTENTORE', 'DIPENDENTE')")
    @GetMapping("/getOrdine/{id_ordine}")
    public ResponseEntity<OrdineDto> findOrdineById(@PathVariable("id_ordine") Long id)
    {
        OrdineDto ordineResponse = ordineService.findById(id);
        return ResponseEntity.ok().body(ordineResponse);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'MANUTENTORE', 'DIPENDENTE')")
    @PostMapping("/addOrdine")
    public ResponseEntity<OrdineDto> saveNewOrdine(@RequestBody OrdineDto ordineDto) {
        ordineService.save(ordineDto);
        return new ResponseEntity<>(ordineDto, HttpStatus.CREATED);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'MANUTENTORE', 'DIPENDENTE')")
    @DeleteMapping("/deleteOrdine/{id_ordine}")
    public ResponseEntity<String> deleteOrdineById(@PathVariable("id_ordine") Long id)
    {
        ordineService.delete(id);
        String apiResponse = ("Record deleted successfully");
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'DIPENDENTE', 'MANUTENTORE')")
    @PatchMapping("/update/{id_ordine}")
    public ResponseEntity<Ordine> patchOrdine(@PathVariable("id_ordine") Long id,
                                              @Validated @RequestBody OrdineUpdateRequest request)
    {
        Ordine ordineAggiornato = ordineService.update(id, request.getOrdineDto(), request.getIdProdotti());

        return ResponseEntity.ok().body(ordineAggiornato);
    }
    @PreAuthorize("hasAnyAuthority('ADMIN', 'DIPENDENTE', 'MANUTENTORE')")
    @PostMapping("/ordini/filtri.dinamici")
    public ResponseEntity<?> filterOrdiniDinamici(@Validated @RequestBody FilterRequestOrdini filterRequest) {
        Map<String, Object> filters = new HashMap<>();

        if (filterRequest.getIdCliente() != null) {
            filters.put("idCliente", filterRequest.getIdCliente());
        }

        if (filterRequest.getIdUtente() != null) {
            filters.put("idUtente", filterRequest.getIdUtente());
        }

        if (filterRequest.getYear() != null) {
            filters.put("year", filterRequest.getYear());
        }

        if (filterRequest.getIdProdotto() != null) {
            filters.put("idProdotto", filterRequest.getIdProdotto());
        }

        String sortBy = filterRequest.getSortBy();
        String sortOrder = filterRequest.getSortOrder();

        if (sortBy != null && sortOrder != null) {
            filters.put("sortBy", sortBy);
            filters.put("sortOrder", sortOrder);
        }

        int page = filterRequest.getPage();
        int size = filterRequest.getSize();

        Page<OrdineDto> filteredResults = (Page<OrdineDto>) ordineService.applyFilters(filters, page, size, sortBy, sortOrder);
        return new ResponseEntity<>(filteredResults, HttpStatus.OK);
    }
}
