package com.myproject.auth.caffetteriaremastered.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "prodotto")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Prodotto {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id_prodotto")
    private Long id_prodotto;

    private String nome_prodotto;

    private String descrizione;

    @Column(name = "prezzoIngrosso")
    private Double prezzoIngrosso;

    @Column(name = "prezzoDettaglio")
    private Double prezzoDettaglio;

    private Integer quantita;

    @OneToMany(mappedBy = "prodotto")
    @JsonIgnore
    private Set<Prodotti_Ordini> prodottiOrdini = new HashSet<>();

    @OneToMany(mappedBy = "prodotto", cascade = CascadeType.ALL)
    @JsonIgnore
    private Set<CategoriaProdotti> categoriaProdotti = new HashSet<>();


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Prodotto prodotto = (Prodotto) o;
        return Objects.equals(id_prodotto, prodotto.id_prodotto);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id_prodotto);
    }

    public void setCategoriaProdotti(List<Categoria> categorie) {
        Set<CategoriaProdotti> nuoveCategorie = new HashSet<>();
        for (Categoria categoria : categorie) {
            CategoriaProdotti categoriaProdotto = new CategoriaProdotti();
            categoriaProdotto.setProdotto(this);
            categoriaProdotto.setCategoria(categoria);
            nuoveCategorie.add(categoriaProdotto);
        }
        this.categoriaProdotti = nuoveCategorie;
    }
}
