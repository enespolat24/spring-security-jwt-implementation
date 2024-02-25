package org.enesp.authservice.controller;

import org.enesp.authservice.model.dto.AuthUserDTO;
import org.enesp.authservice.model.response.AuthResponse;
import org.enesp.authservice.model.AuthUser;
import org.enesp.authservice.service.AuthUserService;
import org.enesp.authservice.service.JwtService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.enesp.authservice.repository.AuthUserRepository;

@RestController
@RequestMapping("/api/v1/authenticate")
public class UserController {

    private final AuthUserService authUserService;

    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public UserController(AuthUserRepository authUserRepository, AuthUserService authUserService, PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.authUserService = authUserService;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    @PostMapping("/register")
    public AuthResponse createUser(@RequestBody AuthUser user) {
        if (authUserService.isUsernameInUse(user.getUsername())) {
            return new AuthResponse(400, "username already in use", null);
        } else if (authUserService.isEmailInUse(user.getEmail())) {
            return new AuthResponse(400, "email already in use", null);
        }
        AuthUserDTO userDto = new AuthUserDTO(user);
        if (authUserService.createUser(userDto) != null) {
            return new AuthResponse(200, "User created successfully", jwtService.generateToken(user.getUsername()));
        }
        return new AuthResponse(500, "Internal server error", null);
    }

    @PostMapping("/login")
    public AuthResponse login(@RequestBody AuthUser user) {
        AuthUserDTO existingUser = authUserService.getUser(user.getUsername());
        if (existingUser != null && passwordEncoder.matches(user.getPassword(), existingUser.password())) {
            return new AuthResponse(200, "Login successful", jwtService.generateToken(existingUser.username()));
        } else {
            return new AuthResponse(401, "username or password is invalid", null);
        }
    }
}
