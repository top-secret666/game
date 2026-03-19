package ru.vitrailclinic.dto;

import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

public class RegisterRequest {
    @NotBlank(message = "Username is required")     
    @Size(min = 3, max = 50)
    private String username;

    @NotBlank(message = "Email is required")     
    @Email(message = "Invalid email format")
    private String email;

    @NotBlank(message = "Password is required")  
    @Size(min = 3, max = 100, message = "Password must be at least 6 characters")   
    private String password;

    @Min(value = 1)
    private int level;

    @NotNull(message = "Experience cannot be null")
    @PositiveOrZero
    private Long experience;

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public int getLevel() { return level; }
    public void setLevel(int level) { this.level = level; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public Long getExperience() { return experience; }
    public void setExperience(Long experience) { this.experience = experience; }
}