package com.myproject.auth.caffetteriaremastered.service;

public interface TokenBlacklistService {
    void addToBlacklist(String token);
    boolean isTokenBlacklisted(String token);
}
