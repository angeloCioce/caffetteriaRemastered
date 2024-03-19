package com.myproject.auth.caffetteriaremastered.controller;

import com.myproject.auth.caffetteriaremastered.dto.AuthenticationRequest;
import com.myproject.auth.caffetteriaremastered.dto.AuthenticationResponse;
import com.myproject.auth.caffetteriaremastered.dto.RegisterRequest;
import com.myproject.auth.caffetteriaremastered.dto.UtenteDto;
import com.myproject.auth.caffetteriaremastered.model.Utente;
import com.myproject.auth.caffetteriaremastered.service.AuthenticationService;
import com.myproject.auth.caffetteriaremastered.service.UtenteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/utente")
@RequiredArgsConstructor
public class UtenteController {

    @Autowired
    private UtenteService utenteService;
    @Autowired
    private final AuthenticationService authenticationService;

    @PostMapping("/register")
    public ResponseEntity<?> register(
            @Valid @RequestBody RegisterRequest request, BindingResult result
    ) {

        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body("Errore di validazione: " + result.getFieldError().getDefaultMessage());
        }

        return ResponseEntity.ok(authenticationService.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(
            @RequestBody AuthenticationRequest request
    ) {

        return ResponseEntity.ok(authenticationService.authenticate(request));
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'DIPENDENTE', 'MANUTENTORE')")
    @DeleteMapping("/delete/{id_utente}")
    public ResponseEntity<String> deleteUtenteById(@PathVariable("id_utente") Long id)
    {
        utenteService.delete(id);
        String apiResponse = ("Record deleted succesfully");
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'DIPENDENTE', 'MANUTENTORE')")
    @PatchMapping("/update/{id_utente}")
    public ResponseEntity<Utente> patchUtente(@PathVariable("id_utente") Long id, @Validated @RequestBody UtenteDto utenteDto)
    {
        Utente updateUtente = utenteService.update(id, utenteDto);
        return new ResponseEntity<>(updateUtente, HttpStatus.OK);
    }
}
