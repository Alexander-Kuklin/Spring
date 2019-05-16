package com.epam.spring.service;

import com.epam.spring.entity.User;

import javax.security.auth.login.FailedLoginException;

public interface UserService {

    User validateUserPassword(String email, String password) throws FailedLoginException;

}
