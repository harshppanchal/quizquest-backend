package com.quizquestbackend.dto;

import com.quizquestbackend.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AuthUserDTO {
    private Long id;
    private String username;
    private Role role;
}