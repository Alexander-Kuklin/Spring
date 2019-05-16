package com.epam.spring.entity;

import com.epam.spring.exception.ExceptionFactory;

public enum OrderStatus {
    CART(0),
    WAITING_TO_BE_CHECKED(1),
    WAITING_FOR_PAYMENT(2),
    ORDER_HAS_BEENPAID(3),
    CANCELED(8);

    private int value;

    public int getValue() {
        return value;
    }

    OrderStatus(int value) {
        this.value = value;
    }

    public static OrderStatus getOrderStatusFromIndex(int index) {
        switch (index) {
            case 0:
                return OrderStatus.CART;
            case 1:
                return OrderStatus.WAITING_TO_BE_CHECKED;
            case 2:
                return OrderStatus.WAITING_FOR_PAYMENT;
            case 3:
                return OrderStatus.ORDER_HAS_BEENPAID;
            case 8:
                return OrderStatus.CANCELED;

            default:
                throw ExceptionFactory.getIllegalArgumentException("Unknown index:" + index);
        }
    }
}
