package com.myproject.auth.caffetteriaremastered.repository;

import com.myproject.auth.caffetteriaremastered.model.Prodotto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface ProdottoRepository extends JpaRepository<Prodotto, Long> {
    @Query("SELECT DISTINCT p FROM Prodotto p JOIN p.categoriaProdotti cp JOIN cp.categoria c WHERE c.id = :categoriaId")
    Page<Prodotto> findByCategoriaId(@Param("categoriaId") Long categoriaId, Pageable pageable);
    @Query("SELECT p FROM Prodotto p WHERE p.nome_prodotto LIKE %:initial%")
    Page<Prodotto> findByInitial(@Param("initial") String initial, Pageable pageable);

    Page<Prodotto> findAll(Specification<Prodotto> spec, Pageable pageable);

    Optional<Prodotto> findById(Long id);
}
