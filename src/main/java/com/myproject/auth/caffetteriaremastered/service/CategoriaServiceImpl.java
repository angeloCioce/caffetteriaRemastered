package com.myproject.auth.caffetteriaremastered.service;

import com.myproject.auth.caffetteriaremastered.dto.CategoriaDto;
import com.myproject.auth.caffetteriaremastered.model.Categoria;
import com.myproject.auth.caffetteriaremastered.repository.CategoriaRepository;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class CategoriaServiceImpl implements CategoriaService{

    @Autowired
    private CategoriaRepository categoriaRepository;


    @Override
    public Categoria save(@Valid CategoriaDto categoriaDto) {
        validateCategoriaDto(categoriaDto);

        Categoria categoria = new Categoria();
        categoria.setNome(categoriaDto.getNome());
        return categoriaRepository.save(categoria);
    }


    private void validateCategoriaDto(@NotNull CategoriaDto categoriaDto)
    {
        if(!StringUtils.hasLength(categoriaDto.getNome()))
        {
            throw new IllegalArgumentException("Il campo 'nome_categoria' non puo' essere vuoto");
        }
    }
}
