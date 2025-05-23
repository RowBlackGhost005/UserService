package com.marin.UserService.dto;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Data Transfer Objects of Users to be used to send minimal data of users through the APIs.
 * This DTO should be used to send 'users' to avoid showing Users roles to the clients.
 */
public record UserDataDTO (
        @Schema(description = "Identifier of the user" , example = "412")
        int id ,

        @Schema(description = "User's unique username" , example = "Jhon")
        String username
) { }
