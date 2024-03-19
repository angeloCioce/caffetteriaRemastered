package com.myproject.auth.caffetteriaremastered.model;


import com.myproject.auth.caffetteriaremastered.userRole.UtenteGenere;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.sql.Date;
import java.util.HashSet;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "cliente")
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id_cliente")
    private Long id;
    @Column(name = "nome")
    private String nome;
    @Column(name = "cognome")
    private String cognome;
    @Column(name = "data_nascita")
    private Date nascita;
    @Column(name = "email")
    private String email;
    @Enumerated(EnumType.STRING)
    private UtenteGenere genere;

    @OneToMany(mappedBy = "cliente")
    private Set<Ordine> ordine = new HashSet<>();
}
