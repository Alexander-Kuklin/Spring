package com.epam.spring.dao;

import com.epam.spring.entity.Order;
import com.epam.spring.entity.OrderItem;
import com.epam.spring.entity.Product;
import com.epam.spring.entity.User;

import java.util.List;

public interface OrderItemRepository {

    double getOrderSumPrice(int idOrder);

    //OrderItem modifyProductQtyInOrderItem(OrderItem orderItem, User user, int qty);

    <T> T mergeEntity(T entity);

    List<OrderItem> getListOrderItem(Order orderCart);

    void deleteOrderItem(OrderItem orderItem, User user);


    void setOrderItemPrice(OrderItem orderItem, Product product);
}
