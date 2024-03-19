package com.myproject.auth.caffetteriaremastered.repository;

import com.myproject.auth.caffetteriaremastered.model.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoriaRepository extends JpaRepository<Categoria, Long> {
    @Query("SELECT cp.categoria FROM CategoriaProdotti cp WHERE cp.prodotto.id_prodotto = :id")
    List<Categoria> findCategorieByProdottoId(@Param("id") Long id);
}
