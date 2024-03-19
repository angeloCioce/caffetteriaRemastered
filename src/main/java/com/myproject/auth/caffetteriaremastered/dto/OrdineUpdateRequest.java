package com.myproject.auth.caffetteriaremastered.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

@Data
public class OrdineUpdateRequest {
   private OrdineDto ordineDto;
   private List<IdProdottoQuantita> idProdotti;
}
