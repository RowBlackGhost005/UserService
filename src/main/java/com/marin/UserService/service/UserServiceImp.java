package com.marin.UserService.service;

import com.marin.UserService.dto.UserDataDTO;
import com.marin.UserService.dto.UserRegistryDTO;
import com.marin.UserService.entities.Role;
import com.marin.UserService.entities.User;
import com.marin.UserService.exception.NoUserFoundException;
import com.marin.UserService.repository.RoleRepository;
import com.marin.UserService.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of the User Service for managing operations related to the User
 */
@Service
public class UserServiceImp implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;


    /**
     * Registers a User in the database but first encodes its password using Bcrypt
     *
     * @return User persisted in the database
     */
    @Override
    public User registerUser(UserRegistryDTO userRegistry) {
        User user = new User();
        user.setUsername(userRegistry.username());
        user.setPassword(passwordEncoder.encode(userRegistry.password()));

        Role userRole = roleRepository.findByName("User").orElseThrow();

        user.getRoles().add(userRole);

        return userRepository.save(user);
    }

    /**
     * Deletes the User from the database whose User ID matches the given as parameter.
     */
    @Override
    public void deleteUser(int id) {
        userRepository.deleteById(id);
    }

    /**
     * Fetches a User with a username as given as parameter and if found returns its UserDataDTO representation.
     *
     * @throws NoUserFoundException Exception if no user with such username doesn't exist.
     * @return UserDataDTO UserDTO for exposing to the controllers.
     */
    @Override
    public UserDataDTO fetchUserByName(String username) throws NoUserFoundException {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new NoUserFoundException("No user with such username"));

        return new UserDataDTO(user.getId() , user.getUsername());
    }

    /**
     * Fetches all Users registered in the database and converts them to UserDataDTO before storing them in a List to return.
     *
     * @return List UserDataDTO of all registered Users
     */
    @Override
    public List<UserDataDTO> fetchAllUsers() {
        List<User> usersDB = userRepository.findAll();

        List<UserDataDTO> usersData = new ArrayList<>();

        for(User userDB : usersDB){
            usersData.add(new UserDataDTO(userDB.getId() , userDB.getUsername()));
        }

        return usersData;
    }
}
