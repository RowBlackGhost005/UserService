package com.marin.UserService.service;

import com.marin.UserService.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Implementation of the role service for managing operations of the role.
 */
@Service
public class RoleServiceImp implements RoleService{

    @Autowired
    private RoleRepository roleRepository;


}
