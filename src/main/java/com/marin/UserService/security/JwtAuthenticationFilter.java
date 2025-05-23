package com.marin.UserService.security;

import com.marin.UserService.service.CustomUserDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * Establishes the logic for extraction and validation of JWT tokens sent from the Clients to grant them access to this API operations.
 */
@Component
@Order(SecurityProperties.DEFAULT_FILTER_ORDER)
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private JwtUtil jwtUtil;

    private CustomUserDetailsService userDetailService;

    public JwtAuthenticationFilter(JwtUtil jwtUtil, CustomUserDetailsService userDetailService) {
        this.jwtUtil = jwtUtil;
        this.userDetailService = userDetailService;
    }

    /**
     * Determines the request has a barer token.
     * If the request has a token it extracts the subject of the token and tries to match it with a registered user in the database,
     * if the token contains a registered user it then loads its credentials, and it grants him access.
     *
     * If a request has no token is let pass and will only work for auth requests.
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String token = extractJwtFromRequest(request);

        if (token != null) {
            String username = jwtUtil.extractUsername(token);
            UserDetails userDetails = userDetailService.loadUserByUsername(username);

            if (jwtUtil.validateToken(token , userDetails) && userDetails.getUsername().equals(username)) {
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities()
                );

                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }

        filterChain.doFilter(request, response);
    }

    /**
     * Extracts the JWT token from the request header and returns it.
     *
     * @returns JWT token.
     */
    private String extractJwtFromRequest(HttpServletRequest request){
        String authorizationHeader = request.getHeader("Authorization");

        return (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) ? authorizationHeader.substring(7) : null;
    }
}
