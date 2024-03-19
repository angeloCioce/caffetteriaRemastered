package com.myproject.auth.caffetteriaremastered.controller;


import com.myproject.auth.caffetteriaremastered.service.TokenBlacklistService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class LogoutController {

    @Autowired
    private TokenBlacklistService tokenBlacklistService;


    @PostMapping("/logout")
    public ResponseEntity<String> addToBlacklist(HttpServletRequest request) {
        String authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String token = authorizationHeader.substring(7);
            tokenBlacklistService.addToBlacklist(token);
            return ResponseEntity.ok("Logout effettuato con successo");
        } else {
            return ResponseEntity.badRequest().body("Authorization header mancante o non valido");
        }
    }
}
