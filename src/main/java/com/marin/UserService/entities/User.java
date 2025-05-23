package com.marin.UserService.entities;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

/**
 * Entity of User
 * A user represents an authenticated client in this API
 *
 * This entity is mapped to 'Users' table in the database, and it should never store or carry a password in plain text
 */
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(unique = true , nullable = false)
    private String username;

    @Column(length = 255)
    private String password;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles = new HashSet<>();

    public User() {
    }

    public User(int id, String username, String password, Set<Role> role) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.roles = role;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> role) {
        this.roles = role;
    }
}
