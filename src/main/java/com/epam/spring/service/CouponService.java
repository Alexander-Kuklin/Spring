package com.epam.spring.service;

import com.epam.spring.entity.Coupon;
import com.epam.spring.entity.User;

import java.time.LocalDate;

public interface CouponService {

    int addCoupon(int idCategory, String nameCoupon, boolean percent, int discount, int minSum,
                  LocalDate startDateDiscount, LocalDate endDateDiscount, User user);

    Coupon getCoupon(String nameCoupon);

    Coupon getCoupon(int id);
}
