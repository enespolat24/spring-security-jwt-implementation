package org.enesp.authservice.model.dto;

import org.enesp.authservice.model.AuthUser;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

// :TODO: Get rid of record an use kotlin data class for dto
public record AuthUserDTO(String username, String password, String email, Optional<String> token, Optional<String> refreshToken) {
    public AuthUserDTO {
        if (username == null || username.isBlank()) {
            throw new IllegalArgumentException("Username cannot be null or empty");
        }
        if (password == null || password.isBlank()) {
            throw new IllegalArgumentException("Password cannot be null or empty");
        }
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("Email cannot be null or empty");
        }
    }

    public AuthUserDTO(AuthUser user) {
        this(user.getUsername(), user.getPassword(), user.getEmail(), Optional.empty(), Optional.empty());
    }

    public AuthUserDTO(AuthUser user, String token, String refreshToken) {
        this(user.getUsername(), user.getPassword(), user.getEmail(), Optional.of(token), Optional.of(refreshToken));
    }


    public AuthUser toEntity() {
        return new AuthUser(username, password, email);
    }
}
