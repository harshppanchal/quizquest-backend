package com.quizquestbackend.mapper;

import org.springframework.stereotype.Component;
import com.quizquestbackend.dto.AuthUserDTO;
import com.quizquestbackend.security.CustomUserDetails;

@Component
public class AuthUserMapper {
    public AuthUserDTO fromPrincipal(CustomUserDetails user) {
        return new AuthUserDTO(user.getId(),user.getUsername(),user.getRole());
    }
}