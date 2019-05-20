package com.epam.spring.service.impl;

import com.epam.spring.JDBC.JDBCUtils;
import com.epam.spring.entity.Order;
import com.epam.spring.entity.OrderItem;
import com.epam.spring.entity.OrderStatus;
import com.epam.spring.entity.User;
import com.epam.spring.service.OrderService;
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
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@RunWith(PowerMockRunner.class)
@PowerMockRunnerDelegate(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/context_test.xml")
@PrepareForTest(JDBCUtils.class)
public class OrderServiceTest {

    @Autowired
    private OrderService orderService;

    @Autowired
    private BasicDataSource dataSource;

    @Mock
    private Connection connection;

    @Before
    public void setUp() throws SQLException {
        when(dataSource.getConnection()).thenReturn(connection);
    }

    @Test
    public void ShouldReturnListOrderByUserAndOrderStatus() throws SQLException{
        User testUser = new User();
        testUser.setId(3);
        OrderStatus orderStatus = OrderStatus.CART;

        List<Order> expected = new ArrayList<>();

        PowerMockito.mockStatic(JDBCUtils.class);
        when(JDBCUtils.select(eq(connection), anyString(), any(), eq(testUser.getId()), eq(orderStatus.getValue())))
                .thenReturn(expected);

        List<Order> actual = orderService.getListOrderUserWithStatus(testUser, orderStatus);
        Assert.assertThat(actual, Is.is(expected));
    }

    @Test
    public void ShouldReturnListOrderByUser() throws SQLException{
        User testUser = new User();
        testUser.setId(3);

        List<Order> expected = new ArrayList<>();

        PowerMockito.mockStatic(JDBCUtils.class);
        when(JDBCUtils.select(eq(connection), anyString(), any(), eq(testUser.getId())))
                .thenReturn(expected);

        List<Order> actual = orderService.getListOrderUser(testUser);
        Assert.assertThat(actual, Is.is(expected));
    }

    @Test
    public void ShouldReturnIdNewUserOrderWithStatusCartByUser() throws SQLException{
        User testUser = new User();
        testUser.setId(3);

        int expected = 5;

        PowerMockito.mockStatic(JDBCUtils.class);
        when(JDBCUtils.insert(eq(connection), anyString(), any(), eq(testUser.getId()), eq(0),
                eq(OrderStatus.CART.getValue()), any(), any(), eq(testUser.getId()))).thenReturn(expected);

        int actual = orderService.addNewUserOrderWithStatusCart(testUser);
        Assert.assertThat(actual, Is.is(expected));
    }

    @Test
    public void ShouldReturnOrderById() throws SQLException{
        Order expected = new Order();

        PowerMockito.mockStatic(JDBCUtils.class);
        when(JDBCUtils.select(eq(connection), anyString(), any(), eq(3))).thenReturn(expected);

        Order actual = orderService.getOrder(3);
        Assert.assertThat(actual, Is.is(expected));
    }

    @Test
    public void ShouldAddProductWithQtyInOrder() throws SQLException{
        Order order = new Order();
        order.setId(15);
        User user = new User();
        user.setId(4);

        int expected = 4;

        PowerMockito.mockStatic(JDBCUtils.class);
        when(JDBCUtils.insert(eq(connection), anyString(), any(), eq(order.getId()), eq(123), eq(123),
                eq(1), any(), any(), eq(user.getId()))).thenReturn(expected);

        int actual = orderService.addProductQtyInOrder(order, user, 123, 1);
        Assert.assertThat(actual, Is.is(expected));
    }

    @Test
    public void ShouldModifyProductQtyInOrderItem() throws SQLException{
        Order order = new Order();
        order.setId(12);
        List<OrderItem> returnList = new ArrayList<>();
        OrderItem expected = new OrderItem();
        expected.setId(22);
        returnList.add(expected);
        User user = new User();
        user.setId(32);

        PowerMockito.mockStatic(JDBCUtils.class);
        when(JDBCUtils.update(eq(connection), anyString(), any(), eq(123), eq(2), any(),
                eq(user.getId()), eq(order.getId()), eq(123))).thenReturn(returnList);

        OrderItem actual = orderService.modifyProductQtyInOrderItem(order, user, 123, 2);
        Assert.assertThat(actual, Is.is(expected));
    }

//    @Test
//    public void Template() throws SQLException{
//        PowerMockito.mockStatic(JDBCUtils.class);
//        when(JDBCUtils.select(eq(connection), anyString(), any(), eq(3))).thenReturn(expected);
//
//        Order actual = orderService.getOrder(3);
//        Assert.assertThat(actual, Is.is(expected));
//    }


}