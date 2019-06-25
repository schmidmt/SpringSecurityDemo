package com.schmidmt.demo.models;

import org.springframework.security.core.GrantedAuthority;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

@Entity
public class MyGrantAuthority implements GrantedAuthority {

    @Id
    @GeneratedValue
    private int id;

    @NotNull
    @Column(unique = true)
    private String role;

    public MyGrantAuthority() { }

    public MyGrantAuthority(String role) {
        this.role = role;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    @Override
    public String getAuthority() {
        return "ROLE_" + role;
    }
}
