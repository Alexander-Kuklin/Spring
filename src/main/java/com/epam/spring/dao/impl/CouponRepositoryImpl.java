package com.epam.spring.dao.impl;

import com.epam.spring.dao.CouponRepository;
import com.epam.spring.entity.Coupon;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

public class CouponRepositoryImpl implements CouponRepository {

    @Autowired
    private EntityManagerFactory entityManagerFactory;

    @Override
    public void addCoupon(Coupon coupon) {
        EntityManager em = entityManagerFactory.createEntityManager();
        em.persist(coupon);
        em.close();
    }

    @Override
    public Coupon getCoupon(String nameCoupon) {
        EntityManager em = entityManagerFactory.createEntityManager();
        Coupon result = em.createQuery("FROM Coupon WHERE Coupon.nameCoupon = :name", Coupon.class)
                .setParameter("name", nameCoupon)
                .getSingleResult();
        em.close();
        return result;
    }

    @Override
    public Coupon getCoupon(int id) {
        EntityManager em = entityManagerFactory.createEntityManager();
        Coupon coupon = em.find(Coupon.class, id);
        em.close();
        return coupon;
    }

    @Override
    public List<Coupon> getCoupons() {
        EntityManager em = entityManagerFactory.createEntityManager();
        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
        CriteriaQuery<Coupon> couponCriteria = criteriaBuilder.createQuery(Coupon.class);
        Root<Coupon> couponRoot = couponCriteria.from(Coupon.class);
        couponCriteria.select(couponRoot);
        List<Coupon> resultList = em.createQuery(couponCriteria).getResultList();
        em.close();
        return resultList;
    }
}
