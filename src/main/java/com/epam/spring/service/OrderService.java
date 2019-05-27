package com.epam.spring.service;

import com.epam.spring.entity.Coupon;
import com.epam.spring.entity.Order;
import com.epam.spring.entity.OrderItem;
import com.epam.spring.entity.User;

import java.time.LocalDate;
import java.util.List;

public interface OrderService {

    void addProductQtyInCart(int productId, int qty, User user);

    Order getUserCart(User user);

    List<OrderItem> getListOrderItem(Order userOrder);

    List<Order> getListOrderUser(User user);

    Coupon addCoupon(int idCategory, String nameCoupon, boolean percent, int discount, int minSum,
                     LocalDate startDateDiscount, LocalDate endDateDiscount, User user);

    void modifyProductQtyInCart(int idProduct, int qty, User user);

    void deleteProductFromCart(int idProduct, User user);

    void confirmOrderCart(Order order, User user);

}
