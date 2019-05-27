package com.epam.spring.dao;

import com.epam.spring.entity.Order;
import com.epam.spring.entity.OrderItem;
import com.epam.spring.entity.OrderStatus;
import com.epam.spring.entity.User;

import java.util.List;

public interface OrderRepository {

    List<Order> getListOrderUserWithStatus(User user, OrderStatus orderStatus);

    List<Order> getListOrderUser(User user);

    Order getOrder(int idOrder);

    OrderItem addOrderItem(OrderItem orderItem);

    Order addOrder(Order order);

    void changeOrderStatus(Order order, OrderStatus orderStatus, User user);

    void setOrderPrice(int idOrder, double sumPrice);

}
