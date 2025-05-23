package com.marin.UserService.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Data Transfer Object for Users Credentials received in the endpoints.
 * This DTO should be only used for managing data received as Register or Login attempt.
 */
public record UserRegistryDTO (

        @NotBlank(message = "You must provide a username")
        @Schema(description = "User's unique username" , example = "Jhon")
        String username ,

        @NotBlank(message = "You must provide a password")
        @Size(min = 4 , message = "Password must have at least 4 characters")
        @Schema(description = "User password, it must be at least 4 characters long" , example = "jhonkey")
        String password
){}
