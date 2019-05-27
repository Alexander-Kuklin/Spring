package com.epam.spring.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Getter
@Setter
@Entity
@Table(name = "order_item")
public class OrderItem extends AbstractEntity<Integer> {

    private static final long serialVersionUID = 3371622096807086892L;

    @Column(name = "id_order")
    private int idOrder;
    @Column(name = "id_product")
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
