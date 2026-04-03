package com.fitness.userservice.DTO;

import com.fitness.userservice.model.User;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * DTO for {@link User}
 */
@Data
public class RegisterRequest {

    String keycloakId;
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
