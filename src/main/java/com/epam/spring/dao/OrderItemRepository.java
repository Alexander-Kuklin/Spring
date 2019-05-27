package com.epam.spring.dao;

import com.epam.spring.entity.OrderItem;
import com.epam.spring.entity.Product;
import com.epam.spring.entity.User;

import java.util.List;

public interface OrderItemRepository {

    double getOrderSumPrice(int idOrder);

    OrderItem modifyProductQtyInOrderItem(OrderItem orderItem, User user, int qty);

    List<OrderItem> getListOrderItem(int idOrderCart);

    void deleteOrderItem(OrderItem orderItem, User user);


    void setOrderItemPrice(OrderItem orderItem, Product product);
}
