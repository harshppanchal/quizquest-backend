package com.quizquestbackend.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import jakarta.validation.constraints.Size;

@Getter
public class UserRegisterDTO {
	@Size(min = 3, max = 30)		
    @NotBlank(message = "Username is required")
    private String username;
    @Email(message = "Invalid email format")
    @NotBlank(message = "Email is required")
    private String email;
    @NotBlank(message = "Password is required")
    @Size(min = 6, message = "Password must be at least 6 characters")
    private String password;   
}