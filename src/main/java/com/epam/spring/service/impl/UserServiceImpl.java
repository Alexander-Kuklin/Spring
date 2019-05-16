package com.epam.spring.service.impl;

import com.epam.spring.JDBC.JDBCUtils;
import com.epam.spring.JDBC.ResultSetHandler;
import com.epam.spring.JDBC.ResultSetHandlerFactory;
import com.epam.spring.entity.User;
import com.epam.spring.exception.ExceptionFactory;
import com.epam.spring.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.security.auth.login.FailedLoginException;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class UserServiceImpl implements UserService {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);

    private final ResultSetHandler<User> userResultSetHandler =
            ResultSetHandlerFactory.getEntityResultSetHandler(ResultSetHandlerFactory.USER_RESULT_SET_HANDLER);

    private final DataSource dataSource;

    public UserServiceImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public User validateUserPassword(String email, String password) throws FailedLoginException {
        try (Connection connection = dataSource.getConnection()) {
            User user = JDBCUtils.select(connection, "SELECT * FROM \"user\" WHERE email = lower(?) AND " +
                    "password = crypt(?, password);", userResultSetHandler, email, password);
            if (user == null) throw ExceptionFactory.getFailedLoginException("Wrong user name or password.");
            if (!user.isActive()) {
                throw ExceptionFactory.getAccessDeniedException("Account user " + user.getName() + " is disabled. " +
                        "Please contact to administrator");
            }
            return user;
        } catch (SQLException e) {
            LOGGER.error("Can't execute SQL request: " + e.getMessage());
            throw ExceptionFactory.getInternalServerErrorException("Can`t execute SQL request: " + e.getMessage(), e);
        }
    }


}
