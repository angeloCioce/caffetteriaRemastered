package com.myproject.auth.caffetteriaremastered.service;

import com.myproject.auth.caffetteriaremastered.model.TokenBlacklistEntity;
import com.myproject.auth.caffetteriaremastered.repository.TokenBlacklistRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TokenBlacklistServiceImpl implements TokenBlacklistService {

    @Autowired
    private TokenBlacklistRepository tokenBlacklistRepository;

    @Override
    public void addToBlacklist(String token) {
        TokenBlacklistEntity tokenBlacklistEntity = new TokenBlacklistEntity();
        tokenBlacklistEntity.setToken(token);
        tokenBlacklistRepository.save(tokenBlacklistEntity);
    }

    @Override
    public boolean isTokenBlacklisted(String token) {
        return tokenBlacklistRepository.existsByToken(token);
    }
}
