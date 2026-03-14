package com.fitness.userservice.DTO;

import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.Value;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * DTO for {@link com.fitness.userservice.model.User}
 */
@Data

public class UserResponse  {


    @NotBlank
    String id;
    String name;
    String email;
    String password;
    String firstname;
    String lastname;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
}