package com.epam.spring.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.LocalDate;

@Getter
@Setter
@ToString
@Entity
@Table(name = "coupon")
public class Coupon extends AbstractEntity {

    private static final long serialVersionUID = -35346256235234268L;

    @Column(name = "id_product_category")
    private int idCategory;
    @Column(name = "coupon", length = 15)
    private String nameCoupon;
    private boolean percent;
    private int discount;
    @Column(name = "min_sum")
    private int minSum;
    @Column(name = "start_date_discount")
    private LocalDate startDateDiscount;
    @Column(name = "stop_date_discount")
    private LocalDate endDateDiscount;
}
