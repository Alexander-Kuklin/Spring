package com.epam.spring.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "order_user")
public class Order extends AbstractEntity {

    private static final long serialVersionUID = 5004398605919935965L;

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

    @Column(name = "orderStatus_id")
    int orderStatus;
//    OrderStatus orderStatus;

    @ManyToOne(targetEntity = User.class, fetch = FetchType.LAZY)//
    private User user;

    @OneToMany(mappedBy = "order", fetch = FetchType.LAZY)
    private List<OrderItem> orderItem;
}
