package com.epam.spring.repository.impl;

import com.epam.spring.entity.User;
import com.epam.spring.service.UserService;

import javax.security.auth.login.FailedLoginException;

public class UserRepositoryImpl {

    private UserService userService;

    public User validateUserPassword(String login, String password) throws FailedLoginException {
        return userService.validateUserPassword(login, password);
    }


    public void setUserService(UserService userService) {
        this.userService = userService;
    }
}
