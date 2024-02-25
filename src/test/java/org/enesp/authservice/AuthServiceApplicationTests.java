package org.enesp.authservice;

import jakarta.transaction.Transactional;
import org.enesp.authservice.model.dto.AuthUserDTO;
import org.enesp.authservice.service.AuthUserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class AuthServiceApplicationTests {

    @Autowired
    private AuthUserService authUserService;
}
