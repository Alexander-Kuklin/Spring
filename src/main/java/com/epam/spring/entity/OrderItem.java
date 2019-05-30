package com.epam.spring.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "order_item")
public class OrderItem extends AbstractEntity {

    private static final long serialVersionUID = 3371622096807086892L;

    private double price;

    private int qty;

    @ManyToOne(fetch = FetchType.EAGER, targetEntity = Order.class)
    @JoinColumn(name = "order_id")
    private Order order;

    @OneToOne
    private Product product;

    @Override
    public String toString() {
        return "OrderItem id=" + getId() +
                ", idOrder=" + order +
                ", idProduct=" + product +
                ", price=" + price +
                ", qty=" + qty;
    }
}
