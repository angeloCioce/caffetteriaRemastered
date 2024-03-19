package com.myproject.auth.caffetteriaremastered.service;

import com.myproject.auth.caffetteriaremastered.dto.UtenteDto;
import com.myproject.auth.caffetteriaremastered.model.Utente;
import org.springframework.data.domain.Page;

public interface UtenteService {
    Utente save(UtenteDto utenteDto);

    Utente findById(Long id);

    Page<Utente> getByRuolo(String ruolo, int page, int size);

    void delete(Long id);

    Utente update(Long id, UtenteDto utenteDto);

    void changePassword(Long id, String newPassword);
}
