package com.quizquestbackend.serviceimpl;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import com.quizquestbackend.entity.User;
import com.quizquestbackend.repo.UserRepository;
import com.quizquestbackend.security.CustomUserDetails;

@Service
public class CustomUserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;
    public CustomUserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String input) {
    	System.out.println("Searching for user: " + input);
    	System.out.println(userRepository.findAll());
        User user = userRepository.findByUsername(input)
            .or(() -> userRepository.findByEmail(input))
            .orElseThrow(() ->
                new UsernameNotFoundException("User not found")
            );

        return new CustomUserDetails(user);
    }
}