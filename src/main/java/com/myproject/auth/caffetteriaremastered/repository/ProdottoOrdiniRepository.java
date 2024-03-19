package com.myproject.auth.caffetteriaremastered.repository;

import com.myproject.auth.caffetteriaremastered.model.Ordine;
import com.myproject.auth.caffetteriaremastered.model.Prodotti_Ordini;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProdottoOrdiniRepository extends JpaRepository<Prodotti_Ordini, Long> {
    List<Prodotti_Ordini> findByOrdine(Ordine ordine);

    @Modifying
    @Query("DELETE FROM Prodotti_Ordini po WHERE po.ordine.id_ordine = :idOrdine")
    void deleteByOrdineId(@Param("idOrdine") Long idOrdine);

    @Query("SELECT po FROM Prodotti_Ordini po WHERE po.ordine.id_ordine = ?1 AND po.prodotto.id_prodotto = ?2")
    Optional<Prodotti_Ordini> findByOrdineIdAndProdottoId(Long ordineId, Long prodottoId);

    @Query("SELECT po FROM Prodotti_Ordini po WHERE po.ordine.id_ordine = :idOrdine")
    List<Prodotti_Ordini> findByOrdineId(Long idOrdine);
}
