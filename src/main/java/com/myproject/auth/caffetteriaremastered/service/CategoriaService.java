package com.myproject.auth.caffetteriaremastered.service;

import com.myproject.auth.caffetteriaremastered.dto.CategoriaDto;
import com.myproject.auth.caffetteriaremastered.model.Categoria;

public interface CategoriaService {

    Categoria save(CategoriaDto categoriaDto);
}
