package com.epam.spring.service.impl;

import com.epam.spring.JDBC.JDBCUtils;
import com.epam.spring.entity.User;
import com.epam.spring.exception.AccessDeniedException;
import com.epam.spring.exception.InternalServerErrorException;
import com.epam.spring.service.UserService;
import org.apache.commons.dbcp2.BasicDataSource;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.modules.junit4.PowerMockRunnerDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.security.auth.login.FailedLoginException;
import javax.sql.DataSource;
import java.sql.Connection;

import static org.hamcrest.core.Is.is;
import static org.mockito.ArgumentMatchers.*;
import static org.powermock.api.mockito.PowerMockito.when;

@RunWith(PowerMockRunner.class)
@PowerMockRunnerDelegate(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/context_test.xml")
@PrepareForTest(JDBCUtils.class)
public class UserServiceTest {

    @Autowired
    private UserService userService;

    @Autowired
    private BasicDataSource dataSource;

    @Mock
    private Connection connection;

    @Before
    public void setUp() throws Exception{
        when(dataSource.getConnection()).thenReturn(connection);
    }

    @Test
    public void ShouldReturnUserEntityWhenEnterCorrectUserNameAndPassword() throws Exception {
        User user = new User();
        user.setId(1);
        user.setName("Test");
        user.setActive(true);
        user.setEmail("ivan@exmple.com");
        user.setLastModifyUser(9);

        PowerMockito.mockStatic(JDBCUtils.class);
        when(JDBCUtils.select(eq(connection), anyString(), any(), eq("ivan@exmple.com"), eq("12345")))
                .thenReturn(user);
        User result = userService.validateUserPassword("ivan@exmple.com", "12345");

        Assert.assertNotNull(user);
        Assert.assertThat(result.getName(), is("Test"));
        Assert.assertThat(result.getEmail(), is("ivan@exmple.com"));
        Assert.assertThat(result.isActive(), is(true));
        Assert.assertThat(result.getId(), is(1));
        Assert.assertThat(result.getLastModifyUser(), is(9));
    }

    @Test (expected = FailedLoginException.class)
    public void ShouldThrowExceptionIfWrongUserNameOrPassword() throws Exception{
        PowerMockito.mockStatic(JDBCUtils.class);
        when(JDBCUtils.select(eq(connection), anyString(), any(), eq("ivan@exmple.com"), eq("12345")))
                .thenReturn(null);
        userService.validateUserPassword("ivan@exmple.com", "12345");
    }

    @Test (expected = AccessDeniedException.class)
    public void ShouldThrowExceptionIfUserDisabled() throws Exception{
        User user = new User();
        user.setActive(false);
        PowerMockito.mockStatic(JDBCUtils.class);
        when(JDBCUtils.select(eq(connection), anyString(), any(), eq("ivan@exmple.com"), eq("12345")))
                .thenReturn(user);
        userService.validateUserPassword("ivan@exmple.com", "12345");
    }

    @Test (expected = InternalServerErrorException.class)
    public void ShouldThrowExceptionIfIncorrectSqlRequest() throws Exception{
        PowerMockito.mockStatic(JDBCUtils.class);
        when(JDBCUtils.select(eq(connection), anyString(), any(), eq("ivan@exmple.com"), eq("12345")))
                .thenThrow(new InternalServerErrorException("Can`t execute SQL request: UTest"));
        userService.validateUserPassword("ivan@exmple.com", "12345");
    }
}