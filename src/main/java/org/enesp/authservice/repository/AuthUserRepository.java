package org.enesp.authservice.repository;

import org.enesp.authservice.model.AuthUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthUserRepository extends JpaRepository<AuthUser, Long> {

    AuthUser findByEmail(String email);

    AuthUser findByUsername(String username);

}
