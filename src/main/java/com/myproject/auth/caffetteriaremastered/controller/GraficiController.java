package com.myproject.auth.caffetteriaremastered.controller;

import com.myproject.auth.caffetteriaremastered.dto.ClientePercentualeOrdini;
import com.myproject.auth.caffetteriaremastered.dto.CostiSpeseGrafico;
import com.myproject.auth.caffetteriaremastered.dto.GuadagnoPerditaTotale;
import com.myproject.auth.caffetteriaremastered.dto.ProdottoPercentualeVendite;
import com.myproject.auth.caffetteriaremastered.service.OrdineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/grafici")
public class GraficiController {

    @Autowired
    private OrdineService ordineService;




    @GetMapping("/percentualeProdottiPiuVenduti/{idOrdine}")
    public ResponseEntity<List<ProdottoPercentualeVendite>> percentualeProdottiPiuVenduti(@PathVariable Long idOrdine) {
        List<ProdottoPercentualeVendite> datiGrafico = ordineService.calcolaPercentualeProdottiPiuVenduti(idOrdine);
        return ResponseEntity.ok(datiGrafico);
    }

    @GetMapping("/percentualeProdottiSuTuttiOrdini")
    public ResponseEntity<List<ProdottoPercentualeVendite>> percentualeProdottiSuTuttiOrdini() {
        List<ProdottoPercentualeVendite> datiGrafico = ordineService.calcolaPercentualeProdottiSuTuttiOrdini();
        return ResponseEntity.ok(datiGrafico);
    }

    @GetMapping("/costiSpeseGrafico")
    public ResponseEntity<List<CostiSpeseGrafico>> costiSpeseGrafico() {
        List<CostiSpeseGrafico> datiGrafico = ordineService.calcolaCostiSpeseGrafico();
        return ResponseEntity.ok(datiGrafico);
    }

    @GetMapping("/clienti-piu-ordini-anno")
    public ResponseEntity<List<ClientePercentualeOrdini>> getClientiPiuOrdiniAnno(@RequestParam("anno") int anno) {
        List<ClientePercentualeOrdini> clientiPercentualeOrdini = ordineService.getClientiPercentualeAcquistoAnno(anno);
        return ResponseEntity.ok().body(clientiPercentualeOrdini);
    }

    @GetMapping("/guadagnoPerditaTotale")
    public ResponseEntity<GuadagnoPerditaTotale> guadagnoPerditaTotale() {
        GuadagnoPerditaTotale datiGuadagnoPerdita = ordineService.calcolaGuadagnoPerditaTotale();
        return ResponseEntity.ok(datiGuadagnoPerdita);
    }
}
