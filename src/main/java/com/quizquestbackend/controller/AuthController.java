package com.quizquestbackend.controller;

import com.quizquestbackend.dto.*;
import com.quizquestbackend.mapper.AuthUserMapper;
import com.quizquestbackend.security.CustomUserDetails;
import com.quizquestbackend.security.JwtUtil;
import com.quizquestbackend.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final AuthenticationManager authManager;
    private final JwtUtil jwtUtil;
    private final AuthService authService;
    private final AuthUserMapper authUserMapper;
    public AuthController(AuthenticationManager authManager,JwtUtil jwtUtil,AuthService authService,AuthUserMapper authUserMapper) {
        this.authManager = authManager;
        this.jwtUtil = jwtUtil;
        this.authService = authService;
        this.authUserMapper = authUserMapper;
    }
    @PostMapping("/login")
    public ApiResponse<AuthResultDTO> login(@Valid @RequestBody LoginRequestDTO request) {
        Authentication authentication;
        try {
            authentication = authManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(),request.getPassword()));
        } catch (UsernameNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"User not found");
        } catch (BadCredentialsException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,"Invalid password");
        } catch (DisabledException e) {
        	throw new ResponseStatusException(HttpStatus.FORBIDDEN,"User account is disabled");
        }

        CustomUserDetails user = (CustomUserDetails) authentication.getPrincipal();

        String token = jwtUtil.generateToken(user.getUsername(),user.getRole().name());
        AuthUserDTO authUser = authUserMapper.fromPrincipal(user);
        return new ApiResponse<>(true,"Login successful",new AuthResultDTO(token, authUser));
    }
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/me")
    public ApiResponse<AuthUserDTO> me(Authentication authentication) {
        if (authentication == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "No authentication");
        }
        Object principal = authentication.getPrincipal();
        if (!(principal instanceof CustomUserDetails user)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unexpected principal type");
        }
        return new ApiResponse<>(true,"Authenticated user",authUserMapper.fromPrincipal(user));
    }

    @PostMapping("/register")
    public ApiResponse<UserResponseDTO> register(@Valid @RequestBody UserRegisterDTO dto) {
        UserResponseDTO user = authService.register(dto);
        return new ApiResponse<>(true,"User registered successfully",user);
    }
}