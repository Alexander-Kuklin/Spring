package com.epam.spring.service.impl;

import com.epam.spring.JDBC.JDBCUtils;
import com.epam.spring.entity.Coupon;
import com.epam.spring.entity.User;
import com.epam.spring.service.CouponService;
import org.apache.commons.dbcp2.BasicDataSource;
import org.hamcrest.core.Is;
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
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;


@RunWith(PowerMockRunner.class)
@PowerMockRunnerDelegate(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/context_test.xml")
@PrepareForTest(JDBCUtils.class)
public class CouponServiceTest {

    @Autowired
    private CouponService couponService;

    @Autowired
    private BasicDataSource dataSource;

    @Mock
    private Connection connection;

    @Before
    public void setUp() throws SQLException {
        when(dataSource.getConnection()).thenReturn(connection);
    }

    @Test
    public void ShouldReturnCouponByCouponName() throws SQLException{
        Coupon expected = new Coupon();

        PowerMockito.mockStatic(JDBCUtils.class);
        when(JDBCUtils.select(eq(connection), anyString(), any(), eq("couponName"))).thenReturn(expected);

        Coupon actual = couponService.getCoupon("couponName");
        Assert.assertThat(actual, Is.is(expected));
    }

    @Test
    public void ShouldReturnCouponById() throws SQLException{
        Coupon expected = new Coupon();

        PowerMockito.mockStatic(JDBCUtils.class);
        when(JDBCUtils.select(eq(connection), anyString(), any(), eq(2))).thenReturn(expected);

        Coupon actual = couponService.getCoupon(2);
        Assert.assertThat(actual, Is.is(expected));
    }

    @Test
    public void ShouldAddCouponAndReturnId() throws SQLException{
        User testUser = new User();
        testUser.setId(2);
        int expected = 5;

        PowerMockito.mockStatic(JDBCUtils.class);
        when(JDBCUtils.insert(eq(connection), anyString(), any(), eq(3), eq("couponName"), eq(true)
                ,eq(5), eq(500), eq(LocalDate.of(2019,01,01)),
                eq(LocalDate.of(2019,12,31)), any(), any(), eq(testUser.getId())))
                .thenReturn(expected);

        int actual = couponService.addCoupon(3, "couponName", true, 5,
                500, LocalDate.of(2019,01,01),
                LocalDate.of(2019,12,31), testUser);
        Assert.assertThat(actual, Is.is(expected));
    }
}