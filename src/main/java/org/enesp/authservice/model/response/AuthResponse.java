package org.enesp.authservice.model.response;

public record AuthResponse(int StatusCode, String Message, String Token, String RefreshToken) {
    public AuthResponse {
        if (StatusCode < 0) {
            throw new IllegalArgumentException("StatusCode must be greater than or equal to 0");
        }
    }
}
