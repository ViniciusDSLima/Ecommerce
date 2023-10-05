package com.example.ecommerce.service.interfaces;

import com.example.ecommerce.entity.Users;
import com.example.ecommerce.exception.UserException;
import jdk.jshell.spi.ExecutionControl;

public interface UserService {

    public Users findUserById(Long userId) throws UserException;
    public Users findUserProfileByJWT(String jwt) throws UserException;
}
