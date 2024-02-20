package org.enesp.authservice.controller;

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
    public String createUser(@RequestBody AuthUser user) {
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
        return "{ \"message\": \"User created successfully\", \"token\": \"" + token + "\" }";
    }

    @PostMapping("/login")
    public String login(@RequestBody AuthUser user) {
        var AuthUser = authUserRepository.findByUsername(user.getUsername());
        if (AuthUser == null) {
            return "username or password is invalid";
        } else if (!passwordEncoder.matches(user.getPassword(), AuthUser.getPassword())) {
            return "username or password is invalid";
        }
        return JwtService.generateToken(AuthUser.getUsername());
    }
}
