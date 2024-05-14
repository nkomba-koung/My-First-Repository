package com.cloudbankai.cloudbankai.domain;

import jakarta.validation.constraints.Size;
import org.springframework.stereotype.Repository;

// Annotation to define a repository or an object of the database (if it has a DB)
@Repository
public class User {

    private Integer id;

    @Size(max=30, message = "Le nom d'utilisateur doit avoir au maximum 30 caracteres")
    private String username;
    @Size(max=10, message = "Le mot de passe doit avoir au maximum 10 caracteres")
    private String password;

    // to set the identifier
    private static int index=0;

    public User() {
        this.id = index++;
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
}
