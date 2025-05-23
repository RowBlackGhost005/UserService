package com.marin.UserService.service;

import com.marin.UserService.entities.Role;
import com.marin.UserService.entities.User;
import com.marin.UserService.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

/**
 * Service for managing UserDetails
 * This class encapsulates the logic of wrapping Users in UserDetails for usage in authentication.
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Returns the User Details of a registered User using its username.
     * This User Details is created using data of an User stored in database.
     *
     * @return UserDetails Details of the user if found.
     * @throws UsernameNotFoundException If the username doesn't exist
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User not found"));

        ArrayList<GrantedAuthority> grantedAuthorities = new ArrayList<>();

        for(Role r : user.getRoles()){
            grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_" + r.getName()));
        }

        return new org.springframework.security.core.userdetails.User(user.getUsername() , user.getPassword() , grantedAuthorities);
    }
}
