package com.marin.UserService.service;

import com.marin.UserService.dto.UserDataDTO;
import com.marin.UserService.dto.UserRegistryDTO;
import com.marin.UserService.entities.User;
import com.marin.UserService.exception.NoUserFoundException;

import java.util.List;

/**
 * Interface for the User service
 */
public interface UserService {

    User registerUser(UserRegistryDTO userRegistry);

    void deleteUser(int id);

    UserDataDTO fetchUserByName(String name) throws NoUserFoundException;

    List<UserDataDTO> fetchAllUsers();
}
