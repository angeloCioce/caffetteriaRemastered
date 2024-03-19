package com.myproject.auth.caffetteriaremastered.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "ordine")
public class Ordine {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id_ordine")
    private Long id_ordine;

    @Column(name = "dataOrdine")
    private LocalDateTime dataOrdine;

    @Column(name = "prezzoTotale")
    private Double prezzoTotale;

    @ManyToOne
    @JoinColumn(name = "id_utente", referencedColumnName = "id_utente")
    @JsonBackReference
    private Utente utente;

    @ManyToOne
    @JoinColumn(name = "id_cliente", referencedColumnName = "id_cliente")
    private Cliente cliente;

    @OneToMany(mappedBy = "ordine", cascade = CascadeType.PERSIST)
    @JsonIgnore
    private Set<Prodotti_Ordini> prodottiOrdini = new HashSet<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Ordine ordine = (Ordine) o;
        return Objects.equals(id_ordine, ordine.id_ordine);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id_ordine);
    }
}
