package com.myproject.auth.caffetteriaremastered.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
@PropertySource("classpath:applicationKey.properties")
public class JwtService {
    @Value("${jwt.secret-key}")
    private String SECRET_KEY;
    private final TokenBlacklistService tokenBlacklistService;

    @Autowired
    public JwtService(TokenBlacklistService tokenBlacklistService) {
        this.tokenBlacklistService = tokenBlacklistService;
    }
    private Claims extractAllClaims(String token) {
        return Jwts
                .parserBuilder()                    // we generate the parser
                .setSigningKey(getSignInKey())      // with the SignIn key
                .build()                            // we build the parser
                .parseClaimsJws(token)              // now we can parse the claims from our token
                .getBody();                         // once it's parsed, we can get all the claims from the token
    }


    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public String generateToken (
            Map<String, Object> extraClaims,
            UserDetails userDetails
    ) {
        return Jwts
                .builder()                                                                      // starts the build
                .setClaims(extraClaims)                                                         // set the desired claims
                .setSubject(userDetails.getUsername())                                          // set the subject based on our user
                .setIssuedAt(new Date(System.currentTimeMillis()))                              // set the current time as the issue date
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 24))           // 24h validation
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)                             // sign the preparing token
                .compact();                                                                     // generate and return the token
    }

    public String generateToken (UserDetails userDetails) {
        return generateToken(new HashMap<>(), userDetails);
    }

    private boolean isTokenExpired(String token) {
        return extractClaim(token, Claims::getExpiration).before(new Date());
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    public boolean isTokenBlacklisted(String token) {
        return tokenBlacklistService.isTokenBlacklisted(token);
    }
}

