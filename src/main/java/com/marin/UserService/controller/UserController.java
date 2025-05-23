package com.marin.UserService.controller;

import com.marin.UserService.dto.UserDataDTO;
import com.marin.UserService.exception.NoUserFoundException;
import com.marin.UserService.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Endpoint for users information it holds both endpoints for Users and Admins
 */
@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    UserService userService;

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/profile")
    @Operation(summary = "Returns the current user profile" , description = "Returns the profile of the current authenticated user, it requires to send the bearer JWT token")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200" , description = "Returns the current user profile"),
            @ApiResponse(responseCode = "401" , description = "No valid token was sent")
    })
    public ResponseEntity<UserDataDTO> fetchUserData(@AuthenticationPrincipal UserDetails userDetails) throws NoUserFoundException {
        UserDataDTO userData = userService.fetchUserByName(userDetails.getUsername());

        return ResponseEntity.ok(userData);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/profile/{username}")
    @Operation(summary = "Returns a user profile" , description = "Returns the profile of the user given as path variable, it requires to send an ADMIN bearer JWT token")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200" , description = "Returns the requested user profile"),
            @ApiResponse(responseCode = "401" , description = "No valid token was sent")
    })
    public ResponseEntity<UserDataDTO> fetchUserProfile(@PathVariable String username) throws NoUserFoundException {
        UserDataDTO userData = userService.fetchUserByName(username);

        return ResponseEntity.ok(userData);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping()
    @Operation(summary = "Returns all registered users" , description = "Returns all registered users and all its data, it requires to send an ADMIN bearer JWT token")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200" , description = "Returns the requested user profile"),
            @ApiResponse(responseCode = "401" , description = "No valid token was sent")
    })
    public ResponseEntity<List<UserDataDTO>> fetchAllUsers(){
        List<UserDataDTO> usersData = userService.fetchAllUsers();

        return ResponseEntity.ok(usersData);
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @DeleteMapping("/{id}")
    @Operation(summary = "Deletes an user" , description = "Deletes the user whose ID matches the given as Path variable, it requires to send an ADMIN bearer JWT token")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200" , description = "Returns the requested user profile"),
            @ApiResponse(responseCode = "401" , description = "No valid token was sent")
    })
    public ResponseEntity<String> deleteUser(@PathVariable int id ){
        userService.deleteUser(id);
        return ResponseEntity.ok("User deleted");
    }

    /**
     * Handles exceptions that rise when you try to access a non-registered user profile
     */
    @ExceptionHandler
    public ResponseEntity<String> handleNoUserFoundException(NoUserFoundException ex){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Error trying to fetch user details: " + ex.getMessage());
    }
}
