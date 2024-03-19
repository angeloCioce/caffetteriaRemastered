package com.myproject.auth.caffetteriaremastered.dto;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {
    private String username;
    @Size(min = 8, message = "La password deve essere lunga almeno 8 caratteri.")
    @Pattern.List({
            @Pattern(regexp = ".*[A-Z].*", message = "La password deve contenere almeno una lettera maiuscola."),
            @Pattern(regexp = ".*[@#$%^&+=].*", message = "La password deve contenere almeno un carattere speciale.")
    })
    private String password;
    private String ruolo;
}
