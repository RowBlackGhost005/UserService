package com.marin.UserService.controller;

import com.marin.UserService.dto.UserDataDTO;
import com.marin.UserService.dto.UserRegistryDTO;
import com.marin.UserService.entities.User;
import com.marin.UserService.exception.NoUserFoundException;
import com.marin.UserService.security.JwtUtil;
import com.marin.UserService.service.CustomUserDetailsService;
import com.marin.UserService.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Endpoint for authentication of clients
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;

    private final AuthenticationManager authenticationManager;

    private final CustomUserDetailsService userDetailsService;

    private final JwtUtil jwtUtil;

    @Autowired
    public AuthController(UserService userService, AuthenticationManager authenticationManager, CustomUserDetailsService userDetailsService, JwtUtil jwtUtil) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/register")
    @Operation(summary = "Register an user in the API" , description = "Registers a user credentials to use them to access this API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200" , description = "User registered successfully"),
            @ApiResponse(responseCode = "409" , description = "Username already taken"),
            @ApiResponse(responseCode = "429" , description = "Too many registration attempts")
    })
    public ResponseEntity<String> registerUser(@Valid @RequestBody UserRegistryDTO userDTO){

        try{
            userService.registerUser(userDTO);
            return ResponseEntity.ok("User registered successfully");
        }catch(DataIntegrityViolationException ex){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("This username has already been taken");
        }

    }

    @PostMapping("/login")
    @Operation(summary = "Request access to the API" , description = "Request and grant access to the API if the credentials given in the body are valid")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200" , description = "Access granted with its JWT Token returned"),
            @ApiResponse(responseCode = "401" , description = "Bad credentials"),
            @ApiResponse(responseCode = "429" , description = "Too many login attempts")
    })
    public ResponseEntity<String> login(@RequestBody UserRegistryDTO loginRequest) throws NoUserFoundException {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                loginRequest.username(), loginRequest.password()));

        UserDetails userDetails = userDetailsService.loadUserByUsername(loginRequest.username());

        UserDataDTO user = userService.fetchUserByName(loginRequest.username());

        String jwt = jwtUtil.generateToken(user , userDetails);

        return ResponseEntity.ok(jwt);
    }

}
