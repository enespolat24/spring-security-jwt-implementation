package org.enesp.authservice.controller;

import org.enesp.authservice.model.AuthResponse;
import org.enesp.authservice.model.AuthUser;
import org.enesp.authservice.service.JwtService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.enesp.authservice.repository.AuthUserRepository;

@RestController
@RequestMapping("/api/v1/authenticate")
public class UserController {

    private final AuthUserRepository authUserRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public UserController(AuthUserRepository authUserRepository, PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.authUserRepository = authUserRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    @PostMapping("/register")
    public AuthResponse createUser(@RequestBody AuthUser user) {
        if (authUserRepository.findByEmail(user.getEmail()) != null){
            throw new RuntimeException("email already in use");
        } else if (authUserRepository.findByUsername(user.getUsername()) != null) {
            throw new RuntimeException("username already in use");
        }

        var AuthUser = new AuthUser();
        AuthUser.setUsername(user.getUsername());
        AuthUser.setEmail(user.getEmail());
        AuthUser.setPassword(passwordEncoder.encode(user.getPassword()));
        authUserRepository.save(AuthUser);
        String token = JwtService.generateToken(AuthUser.getUsername());
        return new AuthResponse(200, "User created successfully", token, null);
    }

    @PostMapping("/login")
    public AuthResponse login(@RequestBody AuthUser user) {
        var AuthUser = authUserRepository.findByUsername(user.getUsername());
        if (AuthUser == null) {
            return new AuthResponse(401, "username or password is invalid", null, null);
        } else if (!passwordEncoder.matches(user.getPassword(), AuthUser.getPassword())) {
            return new AuthResponse(401, "username or password is invalid", null, null);
        }
        return new AuthResponse(200, "Login successful", jwtService.generateToken(AuthUser.getUsername()), null);
    }
}
