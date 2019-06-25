package com.schmidmt.demo.models.forms;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class SignupForm {

    @NotNull
    private String username;

    @NotNull
    @Size(min=6)
    private String password;

    @NotNull
    private String confirmPassword;


    public SignupForm() {}

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

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public boolean passwordsMatch() {
        return confirmPassword.equals(password);
    }
}
