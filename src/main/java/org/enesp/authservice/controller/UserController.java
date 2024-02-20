package org.enesp.authservice.controller;

import org.enesp.authservice.model.AuthUser;
import org.enesp.authservice.util.JwtUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.enesp.authservice.repository.UserRepository;

import java.util.List;

@RestController
@RequestMapping("/api/v1/authenticate")
public class UserController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public UserController(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/register")
    public String createUser(@RequestBody AuthUser user) {
        if (userRepository.findByEmail(user.getEmail()) != null){
            throw new RuntimeException("email already in use");
        } else if (userRepository.findByUsername(user.getUsername()) != null) {
            throw new RuntimeException("username already in use");
        }

        var AuthUser = new AuthUser();
        AuthUser.setUsername(user.getUsername());
        AuthUser.setEmail(user.getEmail());
        AuthUser.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(AuthUser);
        String token = JwtUtil.generateToken(AuthUser.getUsername());
        return "{ \"message\": \"User created successfully\", \"token\": \"" + token + "\" }";
    }

    @PostMapping("/login")
    public String login(@RequestBody AuthUser user) {
        var AuthUser = userRepository.findByUsername(user.getUsername());
        if (AuthUser == null) {
            return "username or password is invalid";
        } else if (!passwordEncoder.matches(user.getPassword(), AuthUser.getPassword())) {
            return "username or password is invalid";
        }
        return JwtUtil.generateToken(AuthUser.getUsername());
    }
}
