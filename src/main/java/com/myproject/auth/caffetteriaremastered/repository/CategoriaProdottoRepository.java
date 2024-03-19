package com.myproject.auth.caffetteriaremastered.repository;

import com.myproject.auth.caffetteriaremastered.model.CategoriaProdotti;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoriaProdottoRepository extends JpaRepository<CategoriaProdotti, Long> {

    @Modifying
    @Query("DELETE FROM CategoriaProdotti cp WHERE cp.prodotto.id_prodotto = :idProdotto")
    void deleteByProdottoId(@Param("idProdotto") Long idProdotto);
}
