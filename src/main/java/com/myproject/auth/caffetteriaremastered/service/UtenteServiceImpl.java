package com.myproject.auth.caffetteriaremastered.service;

import com.myproject.auth.caffetteriaremastered.dto.UtenteDto;
import com.myproject.auth.caffetteriaremastered.model.Utente;
import com.myproject.auth.caffetteriaremastered.repository.UtenteRepository;
import com.myproject.auth.caffetteriaremastered.userRole.UserRole;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Optional;

@Service
public class UtenteServiceImpl implements UtenteService, UserDetailsService {

    @Autowired
    private UtenteRepository utenteRepository;
    @Override
    public Utente save(@Valid UtenteDto utenteDto) {
        validateUtenteDto(utenteDto);
        Utente utente = new Utente();
        utente.setUsername(utenteDto.getUsername());
        utente.setPassword(utenteDto.getPassword());
        utente.setRuolo(UserRole.valueOf(utenteDto.getRuolo()));
        return utenteRepository.save(utente);
    }

    private void validateUtenteDto(UtenteDto utenteDto) {
        if (!StringUtils.hasLength(utenteDto.getUsername())){
            throw new IllegalArgumentException(("Il campo 'username' non può essere vuoto"));
        }
        if (!StringUtils.hasLength(utenteDto.getPassword())){
            throw new IllegalArgumentException(("Il campo 'password' non può essere vuoto"));
        }
        if (!StringUtils.hasLength(utenteDto.getRuolo())){
            throw new IllegalArgumentException(("Il campo 'ruolo' non può essere vuoto"));
        }
    }

    @Override
    public Utente findById(Long id) {
        Optional<Utente> result = utenteRepository.findById(id);
        if(result.isPresent()){
            return result.get();
        }
        else{
            throw new IllegalArgumentException("Utente non trovato con id:" + id);
        }
    }

    @Override
    public Page<Utente> getByRuolo(String ruolo, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return utenteRepository.findByRuolo(UserRole.valueOf(ruolo), pageable);
    }

    @Override
    public void delete(Long id) {
        utenteRepository.deleteById(id);
    }

    @Override
    public Utente update(Long id, UtenteDto utenteDto) {
        Optional<Utente> utenteOptional = utenteRepository.findById(id);
        if(utenteOptional.isPresent())
        {
            Utente utente = utenteOptional.get();
            if (utenteDto.getUsername() != null)
            {
                utente.setUsername(utenteDto.getUsername());
            }
            if (utenteDto.getPassword() != null)
            {
                utente.setPassword(utenteDto.getPassword());
            }
            if (utenteDto.getRuolo() != null)
            {
                utente.setRuolo(UserRole.valueOf(utenteDto.getRuolo()));
            }
            return utenteRepository.save(utente);
        } else{
            throw  new IllegalArgumentException("Utente non trovato con ID: " + id);
        }
    }

    @Override
    public Utente loadUserByUsername(String username) throws UsernameNotFoundException {
        return this.utenteRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("Utente non trovato con username: " + username));
    }

    public void changePassword(Long id, String newPassword){
        Utente utente = utenteRepository.findById(id)
                .orElseThrow(()->new IllegalArgumentException("Utente non trovato"));
        utente.setPassword(newPassword);
        utenteRepository.save(utente);
    }

}
