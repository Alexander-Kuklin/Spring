package com.epam.spring.repository.impl;

import com.epam.spring.entity.*;
import com.epam.spring.exception.ExceptionFactory;
import com.epam.spring.parsers.ParseProductToDB;
import com.epam.spring.repository.OrderRepository;
import com.epam.spring.service.CouponService;
import com.epam.spring.service.OrderService;
import com.epam.spring.service.impl.OrderServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.List;

public class OrderRepositoryImpl implements OrderRepository {

    private static final Logger LOGGER = LoggerFactory.getLogger(ParseProductToDB.class);

    //    private ServiceManager serviceManager;
    private OrderService orderService;
    private CouponService couponService;

    public void addProductQtyInCart(int idProduct, int qty, User user) {
        if (qty < 0) {
            throw ExceptionFactory.getIllegalArgumentException("Qty cant be < 0");
        }
        Order userCart = getUserCart(user);
        List<OrderItem> orderItemList = orderService.getListOrderItem(userCart.getId());

        if (orderItemList.size() == 0) {
            orderService.addProductQtyInOrder(userCart, user, idProduct, qty);
            orderService.refreshOrderPrice(userCart.getId());
            return;
        } else {
            for (OrderItem oi : orderItemList) {
                if (oi.getIdProduct() == idProduct) {
                    orderService.modifyProductQtyInOrderItem(userCart, user, idProduct, qty);
                    orderService.refreshOrderPrice(userCart.getId());
                    return;
                }
            }
            orderService.addProductQtyInOrder(userCart, user, idProduct, qty);
            orderService.refreshOrderPrice(userCart.getId());

        }
    }

    public void modifyProductQtyInCart(int idProduct, int qty, User user) {
        if (qty < 0) {
            throw ExceptionFactory.getIllegalArgumentException("Qty cant be < 0");
        }
        if (qty == 0) {
            deleteProductFromCart(idProduct, user);
            return;
        }
        Order userCart = getUserCart(user);
        List<OrderItem> orderItemList = orderService.getListOrderItem(userCart.getId());

        if (orderItemList.size() == 0) {
            throw ExceptionFactory.getIllegalArgumentException("User cart is empty");
        }

        for (OrderItem oi : orderItemList) {
            if (oi.getIdProduct() == idProduct) {
                orderService.modifyProductQtyInOrderItem(userCart, user, idProduct, qty);
                orderService.refreshOrderPrice(userCart.getId());
                return;
            }
        }
        throw ExceptionFactory.getIllegalArgumentException("User cart don`t have this product: " + idProduct);
    }

    public void deleteProductFromCart(int idProduct, User user) {
        Order userCart = getUserCart(user);
        List<OrderItem> orderItemList = orderService.getListOrderItem(userCart.getId());

        for (OrderItem oi : orderItemList) {
            if (oi.getIdProduct() == idProduct) {
                orderService.deleteOrderItem(userCart, idProduct, user);
                orderService.refreshOrderPrice(userCart.getId());
                return;
            }
        }
        throw ExceptionFactory.getIllegalArgumentException("User cart don`t have this product: " + idProduct);
    }

    public Order getUserCart(User user) {
        List<Order> listOrders = orderService.getListOrderUserWithStatus(user, OrderStatus.CART);
        int orderCartId;
        if (listOrders.size() == 0) {
            orderCartId = orderService.addNewUserOrderWithStatusCart(user);
            return orderService.getOrder(orderCartId);
        }
        return listOrders.get(0);
    }

    public List<OrderItem> getListOrderItem(Order userOrder) {
        return orderService.getListOrderItem(userOrder.getId());
    }

    public List<Order> getListOrderUser(User user) {
        return orderService.getListOrderUser(user);
    }

    public Coupon addCoupon(int idCategory, String nameCoupon, boolean percent, int discount, int minSum,
                            LocalDate startDateDiscount, LocalDate endDateDiscount, User user) {
        int couponId = couponService.addCoupon(idCategory, nameCoupon, percent, discount, minSum, startDateDiscount,
                endDateDiscount, user);
        return couponService.getCoupon(couponId);
    }


    public void setOrderService(OrderServiceImpl orderService) {
        this.orderService = orderService;
    }

    public void setCouponService(CouponService couponService) {
        this.couponService = couponService;
    }
}
