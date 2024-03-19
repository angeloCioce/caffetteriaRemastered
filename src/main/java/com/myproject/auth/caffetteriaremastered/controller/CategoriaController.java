package com.myproject.auth.caffetteriaremastered.controller;

import com.myproject.auth.caffetteriaremastered.dto.CategoriaDto;
import com.myproject.auth.caffetteriaremastered.model.Categoria;
import com.myproject.auth.caffetteriaremastered.service.CategoriaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/categoria")
public class CategoriaController {

    @Autowired
    private CategoriaService categoriaService;


    @PreAuthorize("hasAnyAuthority('ADMIN', 'DIPENDENTE', 'MANUTENTORE')")
    @PostMapping("/addCategoria")
    public ResponseEntity<CategoriaDto> saveNewCategoria(@RequestBody CategoriaDto categoriaDto)
    {
        Categoria categoria = categoriaService.save(categoriaDto);
        return new ResponseEntity<>(categoriaDto, HttpStatus.CREATED);
    }
}
