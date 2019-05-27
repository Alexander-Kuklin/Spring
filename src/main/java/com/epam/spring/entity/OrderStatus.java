package com.epam.spring.entity;

import com.epam.spring.exception.ExceptionFactory;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "order_status")
public enum OrderStatus {
    CART(0),
    WAITING_TO_BE_CHECKED(1),
    WAITING_FOR_PAYMENT(2),
    ORDER_HAS_BEEN_PAID(3),
    CANCELED(8);

    @Id
    @Column(name = "id")
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
                return OrderStatus.ORDER_HAS_BEEN_PAID;
            case 8:
                return OrderStatus.CANCELED;

            default:
                throw ExceptionFactory.getIllegalArgumentException("Unknown index:" + index);
        }
    }
}
