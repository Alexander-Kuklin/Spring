package com.epam.spring.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "order_user")
public class Order extends AbstractEntity<Integer> {

    private static final long serialVersionUID = 5004398605919935965L;

//    @Column(name = "id_user", nullable = false)
//    int idUser;
    @Column(name = "id_payment")
    Integer idPayment;
    @Column(name = "id_delivery")
    Integer idDelivery;
    @Column(name = "price_product")
    Double priceProduct;
    @Column(name = "price_delivery")
    Double priceDelivery;
    @Column(name = "price_discount")
    Double priceDiscount;
    @Column(name = "price")
    Double price;
    @Basic
    @Column(name = "order_status", nullable = false)
    OrderStatus orderStatus;
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    private List<OrderItem> orderItem;

    @Override
    public String toString() {
        return "Order id=" + getId() +
                ", idUser=" + user +
                ", idPayment=" + idPayment +
                ", idDelivery=" + idDelivery +
                ", priceProduct=" + priceProduct +
                ", priceDelivery=" + priceDelivery +
                ", priceDiscount=" + priceDiscount +
                ", price=" + price +
                ", orderStatus=" + orderStatus;
    }
}
