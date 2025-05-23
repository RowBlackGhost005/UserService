package com.marin.UserService.config;

import com.marin.UserService.entities.Role;
import com.marin.UserService.entities.User;
import com.marin.UserService.repository.RoleRepository;
import com.marin.UserService.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * Manages the initial setup of the admin account.
 *
 * This component will execute only ONCE in the initial deployment of the app and its credentials should be changed
 */
@Component
public class AdminSetup implements CommandLineRunner {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final RoleRepository roleRepository;

    private final String adminUser;

    private final String adminPass;

    @Autowired
    public AdminSetup(UserRepository userRepository, PasswordEncoder passwordEncoder, RoleRepository roleRepository, @Value("${auth.admin.username}") String adminUser ,@Value("${auth.admin.password}") String adminPass) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
        this.adminUser = adminUser;
        this.adminPass = adminPass;
    }

    @Override
    public void run(String... args) throws Exception {

        // Check if the Admin user already exists.
        if (userRepository.findByUsername(adminUser).isEmpty()) {
            User admin = new User();
            admin.setUsername(adminUser);
            admin.setPassword(passwordEncoder.encode(adminPass)); // Hash password

            Role role = roleRepository.findByName("ADMIN").orElseThrow();

            admin.getRoles().add(role);

            userRepository.save(admin);
        }
    }
}

