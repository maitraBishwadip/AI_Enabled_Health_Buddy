package com.fitness.userservice.services;

import com.fitness.userservice.DTO.RegisterRequest;
import com.fitness.userservice.DTO.UserResponse;
import com.fitness.userservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor


public class UserService {

    private final UserRepository userRepository;
    public UserResponse register(RegisterRequest registerRequest) {

    }
}
