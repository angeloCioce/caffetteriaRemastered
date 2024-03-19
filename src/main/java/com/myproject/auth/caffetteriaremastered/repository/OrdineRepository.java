package com.myproject.auth.caffetteriaremastered.repository;

import com.myproject.auth.caffetteriaremastered.model.Ordine;
import com.myproject.auth.caffetteriaremastered.model.Prodotti_Ordini;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface OrdineRepository extends JpaRepository<Ordine, Long> {
    Page<Ordine> findAll(Specification<Ordine> spec, Pageable pageable);
    @Query("SELECT o FROM Ordine o WHERE YEAR(o.dataOrdine) = :anno")
    List<Ordine> findByDataOrdineYear(@Param("anno") int anno);
}
