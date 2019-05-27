package com.epam.spring.dao;

import com.epam.spring.entity.User;

public interface UserRepository {

    User getUserById(int id);

    void createNewUser(User newUser);

    User validateUserPassword(String email, String password);

}
