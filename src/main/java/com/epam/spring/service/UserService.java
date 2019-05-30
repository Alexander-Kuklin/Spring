package com.epam.spring.service;

import com.epam.spring.entity.User;

public interface UserService {

    User validateUserPassword(String login, String password);

    User getUserById(int id);

}
