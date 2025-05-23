package com.marin.UserService.entities;

import jakarta.persistence.*;

/**
 * Entity of Role
 * A role determines the permissions a client has inside the API.
 *
 * This entity is mapped to 'Roles' table in the database and its name MUST be stored in UPPER CASE.
 */
@Entity
@Table(name = "Roles")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(unique = true , nullable = false)
    private String name;

    public Role() {
    }

    public Role(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
