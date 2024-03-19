package com.myproject.auth.caffetteriaremastered.dto;

import lombok.Data;

import java.sql.Date;

@Data
public class ClienteDto {
    private Long id;
    private String nome;
    private String cognome;
    private Date nascita;
    private String email;
    private String genere;
    private int page;
    private int size;
}
