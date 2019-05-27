package com.epam.spring.service.impl;

import com.epam.spring.dao.CouponRepository;
import com.epam.spring.dao.OrderItemRepository;
import com.epam.spring.dao.OrderRepository;
import com.epam.spring.dao.ProductRepository;
import com.epam.spring.entity.*;
import com.epam.spring.exception.ExceptionFactory;
import com.epam.spring.parsers.ParseProductToDB;
import com.epam.spring.service.OrderService;
import com.epam.spring.util.MyTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.util.List;

public class OrderServiceImpl implements OrderService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ParseProductToDB.class);

    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private OrderItemRepository orderItemRepository;
    @Autowired
    private CouponRepository couponRepository;
    @Autowired
    private ProductRepository productRepository;

    public void addProductQtyInCart(int idProduct, int qty, User user) {
        checkQty(qty);
        Order userCart = getUserCart(user);
        List<OrderItem> orderItemList = orderItemRepository.getListOrderItem(userCart.getId());

        for (OrderItem oi : orderItemList) {
            if (oi.getIdProduct() == idProduct) {
                modifyProductQtyInCart(idProduct, qty, user);
                return;
            }
        }

        OrderItem orderItem = new OrderItem();
        orderItem.setIdOrder(userCart.getId());
        orderItem.setIdProduct(idProduct);
        orderItem.setQty(qty);
        orderItem.setCreateDate(MyTime.now().toLocalDateTime());
        orderItem.setModifyDate(MyTime.now().toLocalDateTime());
        orderItem.setLastModifyUser(user.getId());

        refreshOrderItemPrice(orderRepository.addOrderItem(orderItem));
        refreshOrderPrice(userCart.getId());
    }

    private void modifyOrderItem(List<OrderItem> orderItemList, Order userCart, int idProduct, int qty, User user) {
        for (OrderItem oi : orderItemList) {
            if (oi.getIdProduct() == idProduct) {
                orderItemRepository.modifyProductQtyInOrderItem(oi, user, qty);
                refreshOrderItemPrice(oi);
                refreshOrderPrice(userCart.getId());
                return;
            }
        }
        throw ExceptionFactory.getIllegalArgumentException("User cart don`t have this product: " + idProduct);
    }

    private void refreshOrderItemPrice(OrderItem orderItem) {
        Product productById = productRepository.getProductById(orderItem.getIdProduct());
        orderItemRepository.setOrderItemPrice(orderItem, productById);
    }

    public void modifyProductQtyInCart(int idProduct, int qty, User user) {
        checkQty(qty);
        if (qty == 0) {
            deleteProductFromCart(idProduct, user);
            return;
        }
        Order userCart = getUserCart(user);
        List<OrderItem> orderItemList = orderItemRepository.getListOrderItem(userCart.getId());

        if (orderItemList.size() == 0) {
            throw ExceptionFactory.getIllegalArgumentException("User cart is empty");
        }

        modifyOrderItem(orderItemList, userCart, idProduct, qty, user);

    }

    public void deleteProductFromCart(int idProduct, User user) {
        Order userCart = getUserCart(user);
        List<OrderItem> orderItemList = orderItemRepository.getListOrderItem(userCart.getId());

        for (OrderItem oi : orderItemList) {
            if (oi.getIdProduct() == idProduct) {
                orderItemRepository.deleteOrderItem(oi, user);
                refreshOrderPrice(userCart.getId());
                return;
            }
        }
        throw ExceptionFactory.getIllegalArgumentException("User cart don`t have this product: " + idProduct);
    }

    @Override
    public void confirmOrderCart(Order order, User user) {
        orderRepository.changeOrderStatus(order, OrderStatus.WAITING_TO_BE_CHECKED, user);
    }

    public Order getUserCart(User user) {
        List<Order> listOrders = orderRepository.getListOrderUserWithStatus(user, OrderStatus.CART);
        if (listOrders.size() == 0) {
            Order order = new Order();
            order.setOrderStatus(OrderStatus.CART);
            order.setIdUser(user.getId());
            order.setCreateDate(MyTime.now().toLocalDateTime());
            order.setModifyDate(MyTime.now().toLocalDateTime());
            order.setLastModifyUser(user.getId());
            return orderRepository.addOrder(order);
        }
        return listOrders.get(0);
    }

    public List<OrderItem> getListOrderItem(Order userOrder) {
        return orderItemRepository.getListOrderItem(userOrder.getId());
    }

    public List<Order> getListOrderUser(User user) {
        return orderRepository.getListOrderUser(user);
    }

    public Coupon addCoupon(int idCategory, String nameCoupon, boolean percent, int discount, int minSum,
                            LocalDate startDateDiscount, LocalDate endDateDiscount, User user) {
        Coupon newCoupon = new Coupon();
        newCoupon.setIdCategory(idCategory);
        newCoupon.setNameCoupon(nameCoupon);
        newCoupon.setPercent(percent);
        newCoupon.setDiscount(discount);
        newCoupon.setMinSum(minSum);
        newCoupon.setStartDateDiscount(startDateDiscount);
        newCoupon.setEndDateDiscount(endDateDiscount);
        newCoupon.setCreateDate(MyTime.now().toLocalDateTime());
        newCoupon.setModifyDate(MyTime.now().toLocalDateTime());
        newCoupon.setLastModifyUser(user.getId());
        couponRepository.addCoupon(newCoupon);
        return couponRepository.getCoupon(newCoupon.getNameCoupon());
    }

    private void refreshOrderPrice(int userCartId) {
        double orderSumPrice = orderItemRepository.getOrderSumPrice(userCartId);
        orderRepository.setOrderPrice(userCartId, orderSumPrice);
    }

    private void checkQty(int qty) {
        if (qty < 0) {
            throw ExceptionFactory.getIllegalArgumentException("Qty cant be < 0");
        }
    }
}
