package com.epam.spring.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Order extends AbstractEntity<Integer> {

    private static final long serialVersionUID = 5004398605919935965L;

    int idUser;
    int idPayment;
    int idDelivery;
    double priceProduct;
    double priceDelivery;
    double priceDiscount;
    double price;
    OrderStatus orderStatus;

    @Override
    public String toString() {
        return "Order id=" + getId() +
                ", idUser=" + idUser +
                ", idPayment=" + idPayment +
                ", idDelivery=" + idDelivery +
                ", priceProduct=" + priceProduct +
                ", priceDelivery=" + priceDelivery +
                ", priceDiscount=" + priceDiscount +
                ", price=" + price +
                ", orderStatus=" + orderStatus;
    }
}
