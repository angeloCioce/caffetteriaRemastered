package com.myproject.auth.caffetteriaremastered.service;

import com.myproject.auth.caffetteriaremastered.dto.AuthenticationRequest;
import com.myproject.auth.caffetteriaremastered.dto.AuthenticationResponse;
import com.myproject.auth.caffetteriaremastered.dto.RegisterRequest;
import com.myproject.auth.caffetteriaremastered.exceptionhandler.InvalidPasswordException;
import com.myproject.auth.caffetteriaremastered.model.Utente;
import com.myproject.auth.caffetteriaremastered.repository.UtenteRepository;
import com.myproject.auth.caffetteriaremastered.userRole.UserRole;
import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Validated
@Service
@RequiredArgsConstructor
public class AuthenticationService {

    @Autowired
    private final UtenteRepository repository;
    @Autowired
    private final PasswordEncoder passwordEncoder;
    @Autowired
    private final JwtService jwtService;
    @Autowired
    private final AuthenticationManager authenticationManager;

    public AuthenticationResponse register(RegisterRequest req) {

        if (!isValidPassword(req.getPassword())) {
            String errorMessage = "Errore di validazione: " + getValidationError(req.getPassword());
            throw new InvalidPasswordException(errorMessage, "password.invalid");
        }

        if (usernameExists(req.getUsername())) {
            throw new ValidationException("Username già in uso.");
        }

        UserRole ruolo = getUserRoleFromString(req.getRuolo());

        var utente = Utente
                .builder()
                .username(req.getUsername())
                .password(passwordEncoder.encode(req.getPassword()))
                .ruolo(ruolo)
                .build();
        repository.save(utente);
        var jwtToken = jwtService.generateToken(utente);
        return AuthenticationResponse
                .builder()
                .token(jwtToken)
                .build();
    }

    private UserRole getUserRoleFromString(String ruoloString) {
        for (UserRole role : UserRole.values()) {
            if (role.name().equalsIgnoreCase(ruoloString)) {
                return role;
            }
        }
        return UserRole.DIPENDENTE;
    }

    public AuthenticationResponse authenticate(AuthenticationRequest req) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            req.getUsername(),
                            req.getPassword()
                    )
            );
        } catch (BadCredentialsException ex) {
            throw new BadCredentialsException("Credenziali di accesso non valide. L'username o la password non sono corrette");
        }
        var user = repository.findByUsername(req.getUsername()).orElseThrow();
        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse
                .builder()
                .token(jwtToken)
                .build();
    }

    private boolean isValidPassword(String password) {
        return password != null &&
                password.length() >= 8 &&
                containsUppercaseLetter(password) &&
                containsSpecialCharacter(password);
    }

    private boolean containsUppercaseLetter(String password) {
        return password.chars().anyMatch(Character::isUpperCase);
    }

    private boolean containsSpecialCharacter(String password) {
        return password.matches(".*[@#$%^&+=].*");
    }

    private InvalidPasswordException getValidationError(String password) {

        if (password == null) {
            return new InvalidPasswordException("La password non può essere nulla.", "password.null");
        }

        if (password.length() < 8) {
            return new InvalidPasswordException("La password deve essere lunga almeno 8 caratteri.", "password.length");
        }

        if (!containsUppercaseLetter(password)) {
            return new InvalidPasswordException("La password deve contenere almeno una lettera maiuscola.", "password.uppercase");
        }

        if (!containsSpecialCharacter(password)) {
            return new InvalidPasswordException("La password deve contenere almeno un carattere speciale.", "password.special");
        }

        return null;
    }

    private boolean usernameExists(String username) {
        return repository.findByUsername(username).isPresent();
    }
}
