package com.quizquestbackend.serviceimpl;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.quizquestbackend.dto.UserRegisterDTO;
import com.quizquestbackend.dto.UserResponseDTO;
import com.quizquestbackend.entity.User;
import com.quizquestbackend.entity.Role;
import com.quizquestbackend.mapper.UserMapper;
import com.quizquestbackend.repo.UserRepository;
import com.quizquestbackend.service.AuthService;

@Service
public class AuthServiceImpl implements AuthService {

    private static final Logger log = LoggerFactory.getLogger(AuthServiceImpl.class);

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

        log.info("Attempting to register user with username={}", dto.getUsername());

        if (userRepository.existsByUsername(dto.getUsername())) {
            log.warn("Registration failed: username already exists -> {}",
                     dto.getUsername());
            throw new IllegalArgumentException("Username already exists");
        }
        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new IllegalArgumentException("Email already exists");
        }
        User user = new User();
        user.setUsername(dto.getUsername());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setEmail(dto.getEmail());
        user.setRole(Role.USER);

        User savedUser = userRepository.save(user);

        log.info("User registered successfully with id={} username={}",
                 savedUser.getId(), savedUser.getUsername());

        return userMapper.toResponseDTO(savedUser);
    }
}