package com.myproject.auth.caffetteriaremastered.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TokenBlacklistDto {
    private String token;
}
