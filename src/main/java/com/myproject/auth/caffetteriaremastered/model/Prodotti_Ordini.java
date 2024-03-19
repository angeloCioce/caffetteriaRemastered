package com.myproject.auth.caffetteriaremastered.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "prodotti_ordini")
public class Prodotti_Ordini {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id_prodotti_ordini;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_ordine")
    private Ordine ordine;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_prodotto")
    private Prodotto prodotto;

    @Column(name = "quantitaOrdine")
    private int quantitaOrdine;


    @Override
    public String toString() {
        return "Prodotti_Ordini[id_prodotti_ordini=" + id_prodotti_ordini + ", quantitaOrdine=" + quantitaOrdine + "]";
    }

}
