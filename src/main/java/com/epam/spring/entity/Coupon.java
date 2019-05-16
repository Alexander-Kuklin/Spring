package com.epam.spring.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;

@Getter
@Setter
@ToString
public class Coupon extends AbstractEntity<Integer> {

    private static final long serialVersionUID = -35346256235234268L;

    int idCategory;
    String nameCoupon;
    boolean percent;
    int discount;
    int minSum;
    LocalDate startDateDiscount;
    LocalDate endDateDiscount;
}
