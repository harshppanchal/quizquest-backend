package com.quizquestbackend.mapper;

import org.springframework.stereotype.Component;
import com.quizquestbackend.entity.User;
import com.quizquestbackend.dto.UserResponseDTO;

@Component
public class UserMapper {

    public UserResponseDTO toResponseDTO(User user) {

        if (user == null) {
            return null;
        }

        return UserResponseDTO.builder()
                .id(user.getId())
                .username(user.getUsername())
                .role(user.getRole().name())
                .build();
    }
}