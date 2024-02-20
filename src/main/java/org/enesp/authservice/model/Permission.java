package org.enesp.authservice.model;

import jakarta.persistence.*;

import java.util.Collection;

@Entity
public class Permission {
    @Id
    @GeneratedValue
    private Long id;

    private String name;

    @ManyToMany(mappedBy = "permissions")
    private Collection<Role> roles;
}
