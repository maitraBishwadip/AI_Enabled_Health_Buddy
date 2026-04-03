package com.fitness.userservice.DTO;

import com.fitness.userservice.model.User;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * DTO for {@link User}
 */
@Data

public class UserResponse  {


    @NotBlank
    String id;
    String keycloakId;

    String email;
    String password;
    String firstname;
    String lastname;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
}