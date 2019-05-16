package com.epam.spring.JDBC;

import com.epam.spring.entity.*;
import com.epam.spring.util.ApplicationContextProvider;
import org.springframework.context.ApplicationContext;

import java.util.ArrayList;
import java.util.List;

public final class ResultSetHandlerFactory {
    private static final ApplicationContext appContext = ApplicationContextProvider.getApplicationContext();

    public static final ResultSetHandler<Integer> GET_ID_RESULT_SET_HANDLER = resultSet -> resultSet.getInt("id");

    public static final ResultSetHandler<Product> PRODUCT_RESULT_SET_HANDLER = resultSet -> {
        Product product = appContext.getBean(Product.class);
        product.setId(resultSet.getInt("id"));
        product.setIdCategory(resultSet.getInt("id_category"));
        product.setAlias(resultSet.getString("alias"));
        product.setTitle(resultSet.getString("title"));
        product.setTextShort((resultSet.getString("text_short")));
        product.setTextFull((resultSet.getString("text_full")));
        product.setPrice(resultSet.getDouble("price"));
        product.setQty(resultSet.getInt("qty"));
        product.setDiscount(resultSet.getInt("discount"));
        product.setActive(resultSet.getBoolean("active"));
        product.setCreateDate(resultSet.getTimestamp("create_date").toLocalDateTime());
        product.setModifyDate(resultSet.getTimestamp("modify_date").toLocalDateTime());
        product.setLastModifyUser(resultSet.getInt("last_modify_user"));
        return product;
    };

    public static final ResultSetHandler<OrderItem> ORDER_ITEM_RESULT_SET_HANDLER = resultSet -> {
        OrderItem orderItem = appContext.getBean(OrderItem.class);
        orderItem.setId(resultSet.getInt("id"));
        orderItem.setIdOrder(resultSet.getInt("id_order"));
        orderItem.setIdProduct(resultSet.getInt("id_product"));
        orderItem.setPrice(resultSet.getDouble("price"));
        orderItem.setQty(resultSet.getInt("qty"));
        orderItem.setCreateDate(resultSet.getTimestamp("create_date").toLocalDateTime());
        orderItem.setModifyDate(resultSet.getTimestamp("modify_date").toLocalDateTime());
        orderItem.setLastModifyUser(resultSet.getInt("last_modify_user"));
        return orderItem;
    };

    public static final ResultSetHandler<Order> ORDER_RESULT_SET_HANDLER = resultSet -> {
        Order order = appContext.getBean(Order.class);
        order.setId(resultSet.getInt("id"));
        order.setIdUser(resultSet.getInt("id_user"));
        order.setIdPayment(resultSet.getInt("id_payment"));
        order.setIdDelivery(resultSet.getInt("id_delivery"));
        order.setPriceProduct(resultSet.getDouble("price_product"));
        order.setPriceDelivery(resultSet.getDouble("price_delivery"));
        order.setPriceDiscount(resultSet.getDouble("price_discount"));
        order.setPrice(resultSet.getDouble("price"));
        order.setOrderStatus(OrderStatus.getOrderStatusFromIndex(resultSet.getInt("order_status")));
        order.setCreateDate(resultSet.getTimestamp("create_date").toLocalDateTime());
        order.setModifyDate(resultSet.getTimestamp("modify_date").toLocalDateTime());
        order.setLastModifyUser(resultSet.getInt("last_modify_user"));
        return order;
    };

    public static final ResultSetHandler<ProductCategory> PRODUCT_CATEGORY_RESULT_SET_HANDLER = resultSet -> {
        ProductCategory productCategory = appContext.getBean(ProductCategory.class);
        productCategory.setId(resultSet.getInt("id"));
        productCategory.setIdParent(resultSet.getInt("id_parent"));
        productCategory.setAlias(resultSet.getString("alias"));
        productCategory.setTitle(resultSet.getString("title"));
        productCategory.setText(resultSet.getString("text"));
        productCategory.setActive(resultSet.getBoolean("active"));
        productCategory.setCreateDate(resultSet.getTimestamp("create_date").toLocalDateTime());
        productCategory.setModifyDate(resultSet.getTimestamp("modify_date").toLocalDateTime());
        productCategory.setLastModifyUser(resultSet.getInt("last_modify_user"));
        return productCategory;
    };

    public static final ResultSetHandler<User> USER_RESULT_SET_HANDLER = resultSet -> {
        User user = appContext.getBean(User.class);
        user.setId(resultSet.getInt("id"));
        user.setEmail(resultSet.getString("email"));
        user.setPassword(resultSet.getString("password"));
        user.setName(resultSet.getString("name"));
        user.setActive(resultSet.getBoolean("active"));
        user.setCreateDate(resultSet.getTimestamp("create_date").toLocalDateTime());
        user.setModifyDate(resultSet.getTimestamp("modify_date").toLocalDateTime());
        user.setLastModifyUser(resultSet.getInt("last_modify_user"));
        return user;
    };

    public static final ResultSetHandler<Coupon> COUPON_RESULT_SET_HANDLER = resultSet -> {
        Coupon coupon = appContext.getBean(Coupon.class);
        coupon.setId(resultSet.getInt("id"));
        coupon.setIdCategory(resultSet.getInt("id_product_category"));
        coupon.setNameCoupon(resultSet.getString("coupon"));
        coupon.setPercent(resultSet.getBoolean("percent"));
        coupon.setDiscount(resultSet.getInt("discount"));
        coupon.setMinSum(resultSet.getInt("min_sum"));
        coupon.setStartDateDiscount(resultSet.getDate("start_date_discount").toLocalDate());
        coupon.setEndDateDiscount(resultSet.getDate("stop_date_discount").toLocalDate());
        coupon.setCreateDate(resultSet.getTimestamp("create_date").toLocalDateTime());
        coupon.setModifyDate(resultSet.getTimestamp("modify_date").toLocalDateTime());
        coupon.setLastModifyUser(resultSet.getInt("last_modify_user"));
        return coupon;
    };

    public static <T> ResultSetHandler<List<T>> getListResultSetHandler(final ResultSetHandler<T> oneRowResultSetHandler) {
        return resultSet -> {

            List<T> list = appContext.getBean(ArrayList.class);
            while (resultSet.next()) {
                list.add(oneRowResultSetHandler.handle(resultSet));
            }
            return list;
        };
    }

    public static <T> ResultSetHandler<T> getEntityResultSetHandler(final ResultSetHandler<T> oneRowResultSetHandler) {
        return resultSet -> {
            if (resultSet.next()) {
                return oneRowResultSetHandler.handle(resultSet);
            } else {
                return null;
            }
        };
    }

}
