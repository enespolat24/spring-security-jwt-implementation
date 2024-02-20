package org.enesp.authservice.model;

import jakarta.persistence.*;

import java.util.Collection;

@Entity
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    @ManyToMany(mappedBy = "roles")
    private Collection<AuthUser> authUsers;

    @ManyToMany()
    @JoinTable(
            name = "role_permission",
            joinColumns = @JoinColumn(name = "role_id"),
            inverseJoinColumns = @JoinColumn(name = "permission_id")
    )
    private Collection<Permission> permissions;
}
