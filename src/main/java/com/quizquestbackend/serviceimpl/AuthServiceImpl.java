package com.quizquestbackend.serviceimpl;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import com.quizquestbackend.dto.UserRegisterDTO;
import com.quizquestbackend.dto.UserResponseDTO;
import com.quizquestbackend.entity.User;
import com.quizquestbackend.entity.Role;
import com.quizquestbackend.mapper.UserMapper;
import com.quizquestbackend.repo.UserRepository;
import com.quizquestbackend.service.AuthService;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;

@Service
public class AuthServiceImpl implements AuthService {
    private final BCryptPasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    public AuthServiceImpl(UserRepository userRepository,BCryptPasswordEncoder passwordEncoder,UserMapper userMapper) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.userMapper = userMapper;
    }
    @Override
    public UserResponseDTO register(UserRegisterDTO dto) {
        if (userRepository.existsByUsername(dto.getUsername())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Username already exists");
        }
        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Email already exists");
        }
        try {
        	User user = new User();
            user.setUsername(dto.getUsername());
            user.setPassword(passwordEncoder.encode(dto.getPassword()));
            user.setEmail(dto.getEmail());
            user.setRole(Role.USER);
            User savedUser = userRepository.save(user);
            return userMapper.toResponseDTO(savedUser);
        } catch (DataIntegrityViolationException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Username or email already exists");
        }
    }
}