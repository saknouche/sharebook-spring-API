package com.sadev.sharebook.user;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
@Entity
@Table(name = "userInfo")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String email;
    @Size(min = 2, max = 25, message = "Le nom doit contenir entre 5 et 25 caractères.")
    private String firstName;
    @Size(min = 2, max = 25, message = "Le prénom doit contenir entre 5 et 25 caractères.")

    private String lastName;
    private String password;

    public User() {
    }

    public User(String email) {
        this.email = email;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
