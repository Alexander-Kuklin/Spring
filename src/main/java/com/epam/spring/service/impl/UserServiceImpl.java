package com.epam.spring.service.impl;

import com.epam.spring.dao.UserRepository;
import com.epam.spring.entity.User;
import com.epam.spring.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;

public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    public User validateUserPassword(String login, String password) {
        return userRepository.validateUserPassword(login, password);
    }

    public User getUserById(int id) {
        return userRepository.getUserById(id);
    }
}
