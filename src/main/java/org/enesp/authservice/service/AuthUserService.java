package org.enesp.authservice.service;

import org.enesp.authservice.model.dto.AuthUserDTO;
import org.enesp.authservice.repository.AuthUserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.logging.Logger;

@Service
public class AuthUserService {

    private final AuthUserRepository authUserRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthUserService(AuthUserRepository authUserRepository, PasswordEncoder passwordEncoder, PasswordEncoder passwordEncoder1) {
        this.authUserRepository = authUserRepository;
        this.passwordEncoder = passwordEncoder1;
    }

    public AuthUserDTO getUser(String username) {
        return new AuthUserDTO(authUserRepository.findByUsername(username));
    }

    public boolean isUsernameInUse(String username) {
        return authUserRepository.findByUsername(username) != null;
    }

    public boolean isEmailInUse(String email) {
        return authUserRepository.findByEmail(email) != null;
    }

    public AuthUserDTO createUser(AuthUserDTO user) {
        AuthUserDTO userWithHashedPassword = new AuthUserDTO(user.username(), user.email(), passwordEncoder.encode(user.password()),null, null);
        if (isUsernameInUse(user.username())) {
            throw new RuntimeException("username already in use");
        } else if (isEmailInUse(user.email())) {
            throw new RuntimeException("email already in use");
        }
        try{
          authUserRepository.save(userWithHashedPassword.toEntity());
          return userWithHashedPassword;
        } catch (Exception e) {
            Logger.getGlobal().severe(e.getMessage());
        }
        return null;
    }
}
