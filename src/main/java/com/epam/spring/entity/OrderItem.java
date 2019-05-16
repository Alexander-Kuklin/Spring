package com.epam.spring.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderItem extends AbstractEntity<Integer> {

    private static final long serialVersionUID = 3371622096807086892L;

    private int idOrder;
    private int idProduct;
    private double price;
    private int qty;

    @Override
    public String toString() {
        return "OrderItem id=" + getId() +
                ", idOrder=" + idOrder +
                ", idProduct=" + idProduct +
                ", price=" + price +
                ", qty=" + qty;
    }
}
