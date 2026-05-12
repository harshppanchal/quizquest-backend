package com.quizquestbackend.service;

import com.quizquestbackend.dto.UserRegisterDTO;
import com.quizquestbackend.dto.UserResponseDTO;

public interface AuthService {
    UserResponseDTO register(UserRegisterDTO dto);
}