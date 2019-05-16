package com.epam.spring.service.impl;

import com.epam.spring.JDBC.JDBCUtils;
import com.epam.spring.JDBC.ResultSetHandler;
import com.epam.spring.JDBC.ResultSetHandlerFactory;
import com.epam.spring.entity.Coupon;
import com.epam.spring.entity.User;
import com.epam.spring.exception.ExceptionFactory;
import com.epam.spring.service.CouponService;
import com.epam.spring.util.MyTime;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;

public class CouponServiceImpl implements CouponService {

    private final ResultSetHandler<Coupon> couponResultSetHandler =
            ResultSetHandlerFactory.getEntityResultSetHandler(ResultSetHandlerFactory.COUPON_RESULT_SET_HANDLER);
    private final ResultSetHandler<Integer> returnIdResultSetHandler =
            ResultSetHandlerFactory.getEntityResultSetHandler(ResultSetHandlerFactory.GET_ID_RESULT_SET_HANDLER);

    private final DataSource dataSource;

    public CouponServiceImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public int addCoupon(int idCategory, String nameCoupon, boolean percent, int discount, int minSum,
                         LocalDate startDateDiscount, LocalDate endDateDiscount, User user) {
        try (Connection connection = dataSource.getConnection()) {
            int insert = JDBCUtils.insert(connection,
                    "INSERT INTO coupon (id_product_category, coupon, percent, discount, min_sum, " +
                            "start_date_discount, stop_date_discount, create_date, modify_date, last_modify_user) " +
                            "VALUES (?,?,?,?,?,?,?,?,?,?)",
                    returnIdResultSetHandler, idCategory, nameCoupon, percent, discount, minSum, startDateDiscount,
                    endDateDiscount, MyTime.now(), MyTime.now(), user.getId());
            connection.commit();
            return insert;
        } catch (SQLException e) {
            throw ExceptionFactory.getInternalServerErrorException("Can`t execute SQL request: " + e.getMessage(), e);
        }
    }

    @Override
    public Coupon getCoupon(String nameCoupon) {
        try (Connection connection = dataSource.getConnection()) {
            return JDBCUtils.select(connection,
                    "SELECT * FROM coupon WHERE coupon LIKE ?", couponResultSetHandler, nameCoupon);
        } catch (SQLException e) {
            throw ExceptionFactory.getInternalServerErrorException("Can`t execute SQL request: " + e.getMessage(), e);
        }
    }

    @Override
    public Coupon getCoupon(int id) {
        try (Connection connection = dataSource.getConnection()) {
            return JDBCUtils.select(connection,
                    "SELECT * FROM coupon WHERE id=?", couponResultSetHandler, id);
        } catch (SQLException e) {
            throw ExceptionFactory.getInternalServerErrorException("Can`t execute SQL request: " + e.getMessage(), e);
        }
    }


}
