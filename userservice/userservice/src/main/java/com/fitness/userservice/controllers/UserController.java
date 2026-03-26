package com.fitness.userservice.controllers;


import com.fitness.userservice.DTO.RegisterRequest;
import com.fitness.userservice.DTO.UserResponse;
import com.fitness.userservice.services.UserService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor

public class UserController {

    private final UserService userService;


    @PostMapping("/register")
    public ResponseEntity<UserResponse> register(@RequestBody RegisterRequest registerRequest) {
        UserResponse userResponse = userService.register(registerRequest);
        return ResponseEntity.ok(userResponse);
    }

    @GetMapping( "/{userId}")
    public ResponseEntity<UserResponse> userDetails(@PathVariable  Long userId)
    {
        return ResponseEntity.ok(userService.getUserProfile(userId));
    }


    @GetMapping( "/{userId}/validate")
    public ResponseEntity<Boolean> validateUser(@PathVariable  Long userId)
    {
        return ResponseEntity.ok(userService.existByUserId(userId));
    }








}
