package com.fitness.userservice.DTO;

import com.fitness.userservice.model.UserRole;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.Value;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * DTO for {@link com.fitness.userservice.model.User}
 */
@Data
public class RegisterRequest {

    @NotBlank
    String email;
    @NotNull
    @NotBlank
    String password;
    @NotBlank
    String firstname;
    @NotBlank
    String lastname;

}
