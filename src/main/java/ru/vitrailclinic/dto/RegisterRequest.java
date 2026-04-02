package ru.vitrailclinic.dto;

import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class RegisterRequest {
    @NotBlank(message = "Username is required")     
    @Size(min = 3, max = 50)
    private String username;

    @NotBlank(message = "Email is required")     
    @Email(message = "Invalid email format")
    private String email;

    @NotBlank(message = "Password is required")  
    @Size(min = 8, max = 100, message = "Password must be at least 8 characters")   
    private String password;

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}