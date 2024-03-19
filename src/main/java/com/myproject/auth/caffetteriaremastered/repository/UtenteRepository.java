package com.myproject.auth.caffetteriaremastered.repository;

import com.myproject.auth.caffetteriaremastered.model.Utente;
import com.myproject.auth.caffetteriaremastered.userRole.UserRole;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UtenteRepository extends JpaRepository<Utente, Long> {
    Optional<Utente> findByUsername(String username);
    Page<Utente> findByRuolo(UserRole ruolo, Pageable pageable);
}
