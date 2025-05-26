package com.marin.UserService.security;

import com.marin.UserService.dto.UserDataDTO;
import com.marin.UserService.entities.Role;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.marin.UserService.entities.User;

import javax.crypto.SecretKey;
import java.awt.*;
import java.util.Date;

/**
 * Component for managing JWT tokens.
 * This component manages creation, validation and other operations related to the JWT token.
 */
@Component
public class JwtUtil {

    /**
     * Secret KEY used to sign the tokens.
     * It MUST remain the same throughout the deployment otherwise it will throw validation errors of prior tokens.
     */
    private final SecretKey secretKey;

    /**
     * Determines the length of a day in milliseconds.
     */
    private final int ONE_DAY_MILIS = 86400000;

    /**
     * Creates a JwtUtil object using the secret key stored in the properties of this app.
     */
    private JwtUtil(@Value("${jwt.secret}") String secret){
        secretKey = Keys.hmacShaKeyFor(secret.getBytes());
    }

    /**
     * Generates a JWT token and signs it with the secret key of this object.
     * The JWT tokens contains the signed claim subject for the username of the user to authenticate.
     * This token also contains the roles of the authenticated user.
     *
     * @param user User to be used to generate the token.
     * @return Signed JWT token.
     */
    public String generateToken(UserDataDTO user , UserDetails userDetails) {
        return Jwts.builder()
                .subject(user.username())
                .claim("id" , user.id())
                .claim("roles", userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + ONE_DAY_MILIS))
                .signWith(secretKey , Jwts.SIG.HS256)
                .compact();
    }

    /**
     * Extracts the username of the user used to create the token given as parameter.
     * The username comes in the signed claim subject of the token.
     *
     * @param token Token to user for extraction.
     * @return Username of the user of this token.
     */
    public String extractUsername(String token){
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }

    /**
     * Determines whether a given token is valid or not.
     * A token can be invalid if its either expired or it doesn't match its bearer credentials.
     *
     * @param token Token to validate
     * @param userDetails User details to contrast against the token
     * @return True if the token is valid, False otherwise
     */
    public boolean validateToken(String token , UserDetails userDetails){
        String username = extractUsername(token);

        return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }

    /**
     * Determines whether a token is expired or not based on the time elapsed between its creation and now
     *
     * @param token Token to verify its expiration.
     * @return True if its expired, False otherwise
     */
    private boolean isTokenExpired(String token){
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getExpiration().before(new Date());
    }
}
