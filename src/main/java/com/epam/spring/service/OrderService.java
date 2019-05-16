package com.epam.spring.service;

import com.epam.spring.entity.Order;
import com.epam.spring.entity.OrderItem;
import com.epam.spring.entity.OrderStatus;
import com.epam.spring.entity.User;

import java.util.List;

public interface OrderService {

    List<Order> getListOrderUserWithStatus(User user, OrderStatus orderStatus);

    List<Order> getListOrderUser(User user);

    List<OrderItem> getListOrderItem(int idOrderCart);

    Order getOrder(int idOrder);

    int addProductQtyInOrder(Order order, User user, int idProduct, int qty);

    int addNewUserOrderWithStatusCart(User user);

    OrderItem modifyProductQtyInOrderItem(Order order, User user, int idProduct, int qty);

    void deleteOrderItem(Order order, int idProduct, User user);

    void confirmOrder(User user, int idOrder);

    void refreshOrderPrice(int idOrder);


}
