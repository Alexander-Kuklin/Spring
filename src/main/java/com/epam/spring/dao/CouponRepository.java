package com.epam.spring.dao;

import com.epam.spring.entity.Coupon;

import java.util.List;

public interface CouponRepository {

    void addCoupon(Coupon coupon);

    Coupon getCoupon(String nameCoupon);

    Coupon getCoupon(int id);

    List<Coupon> getCoupons();
}
