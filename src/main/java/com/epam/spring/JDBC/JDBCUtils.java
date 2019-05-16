package com.epam.spring.JDBC;

import com.epam.spring.exception.ExceptionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class JDBCUtils {
    private static final Logger LOGGER = LoggerFactory.getLogger(JDBCUtils.class);

    public static <T> T select(Connection c, String sql, ResultSetHandler<T> resultSetHandler, Object... parameters) throws SQLException {
        try (PreparedStatement ps = c.prepareStatement(sql)) {
            populatePreparedStatement(ps, parameters);
            LOGGER.debug("****PrepSt SELECT: " + ps.toString());
            ResultSet resultSet = ps.executeQuery();
            LOGGER.debug("****ResultSet SELECT: " + resultSet.toString());
            return resultSetHandler.handle(resultSet);
        }
    }

    public static <T> T update(Connection c, String sql, ResultSetHandler<T> resultSetHandler, Object... parameters) throws SQLException {
        try (PreparedStatement ps = c.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            populatePreparedStatement(ps, parameters);
            LOGGER.debug("****PrepSt UPDATE: " + ps.toString());
            int affectedRow = ps.executeUpdate();
            c.commit();
            if (affectedRow == 0) {
                LOGGER.info("Can't update row in database. Result =" + affectedRow + " SQL query: " + ps.toString());
                throw ExceptionFactory.getSqlException("Can't update row in database. Result =" + affectedRow + " SQL query: " + ps.toString());
            }
            ResultSet resultSet = ps.getGeneratedKeys();
            LOGGER.debug("****ResultSet UPDATE: " + resultSet.toString());
            return resultSetHandler.handle(resultSet);
        }
    }

    public static <T> T insert(Connection c, String sql, ResultSetHandler<T> resultSetHandler, Object... parameters) throws SQLException {
        try (PreparedStatement ps = c.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            populatePreparedStatement(ps, parameters);
            LOGGER.debug("****PrepSt INSERT: " + ps.toString());
            int result = ps.executeUpdate();
            if (result != 1) {
                throw ExceptionFactory.getSqlException("Can't insert row to database. Result=" + result + " SQL query: " + ps.toString());
            }
            ResultSet resultSet = ps.getGeneratedKeys();
            LOGGER.debug("****ResultSet INSERT: " + resultSet.toString());
            return resultSetHandler.handle(resultSet);
        }
    }

    private static void populatePreparedStatement(PreparedStatement ps, Object... parameters) throws SQLException {
        if (parameters != null) {
            for (int i = 0; i < parameters.length; i++) {
                ps.setObject(i + 1, parameters[i]);
            }
        }
    }

}
