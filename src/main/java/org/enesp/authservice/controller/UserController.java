package org.enesp.authservice.controller;

import org.enesp.authservice.model.AuthUser;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.enesp.authservice.repository.UserRepository;

import java.util.List;

@RestController
public class UserController {

    private final UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/users")
    public List<AuthUser> getUsers() {
        return userRepository.findAll();
    }

    @GetMapping("/token")
    public String getToken() {
        return "token";
    }
}
