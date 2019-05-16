package com.epam.spring.service.impl;

import com.epam.spring.JDBC.JDBCUtils;
import com.epam.spring.JDBC.ResultSetHandler;
import com.epam.spring.JDBC.ResultSetHandlerFactory;
import com.epam.spring.entity.Order;
import com.epam.spring.entity.OrderItem;
import com.epam.spring.entity.OrderStatus;
import com.epam.spring.entity.User;
import com.epam.spring.exception.ExceptionFactory;
import com.epam.spring.service.OrderService;
import com.epam.spring.util.MyTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class OrderServiceImpl implements OrderService {
    private static final Logger LOGGER = LoggerFactory.getLogger(OrderServiceImpl.class);

    private final ResultSetHandler<List<Order>> orderListResultSetHandler =
            ResultSetHandlerFactory.getListResultSetHandler(ResultSetHandlerFactory.ORDER_RESULT_SET_HANDLER);
    private final ResultSetHandler<Order> orderResultSetHandler =
            ResultSetHandlerFactory.getEntityResultSetHandler(ResultSetHandlerFactory.ORDER_RESULT_SET_HANDLER);
    private final ResultSetHandler<List<OrderItem>> orderItemListResultSetHandler =
            ResultSetHandlerFactory.getListResultSetHandler(ResultSetHandlerFactory.ORDER_ITEM_RESULT_SET_HANDLER);
    private final ResultSetHandler<Integer> returnIdResultSetHandler =
            ResultSetHandlerFactory.getEntityResultSetHandler(ResultSetHandlerFactory.GET_ID_RESULT_SET_HANDLER);

    private final DataSource dataSource;

    public OrderServiceImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }


    @Override
    public List<Order> getListOrderUserWithStatus(User user, OrderStatus orderStatus) {
        try (Connection connection = dataSource.getConnection()) {
            return JDBCUtils.select(connection, "SELECT * FROM order_user WHERE id_user = ?" +
                    "AND order_status = ?;", orderListResultSetHandler, user.getId(), orderStatus.getValue());
        } catch (SQLException e) {
            throw ExceptionFactory.getInternalServerErrorException("Can`t execute SQL request: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Order> getListOrderUser(User user) {
        try (Connection connection = dataSource.getConnection()) {
            return JDBCUtils.select(connection, "SELECT * FROM order_user WHERE id_user = ?;",
                    orderListResultSetHandler, user.getId());
        } catch (SQLException e) {
            throw ExceptionFactory.getInternalServerErrorException("Can`t execute SQL request: " + e.getMessage(), e);
        }
    }

    @Override
    public int addNewUserOrderWithStatusCart(User user) {
        try (Connection connection = dataSource.getConnection()) {
            int insert = JDBCUtils.insert(connection, "INSERT INTO order_user (id_user, price, order_status, create_date, " +
                            "modify_date, last_modify_user) VALUES (?,?,?,?,?,?);", returnIdResultSetHandler,
                    user.getId(), 0, OrderStatus.CART.getValue(), MyTime.now(), MyTime.now(), user.getId());
            connection.commit();
            return insert;
        } catch (SQLException e) {
            throw ExceptionFactory.getInternalServerErrorException("Can`t execute SQL request: " + e.getMessage(), e);
        }
    }

    @Override
    public Order getOrder(int idOrder) {
        try (Connection connection = dataSource.getConnection()) {
            return JDBCUtils.select(connection, "SELECT * FROM order_user WHERE id=?", orderResultSetHandler, idOrder);
        } catch (SQLException e) {
            throw ExceptionFactory.getInternalServerErrorException("Can`t execute SQL request: " + e.getMessage(), e);
        }
    }

    @Override
    public int addProductQtyInOrder(Order order, User user, int idProduct, int qty) {
        try (Connection connection = dataSource.getConnection()) {
            int insert = JDBCUtils.insert(connection,
                    "INSERT INTO order_item (id_order, id_product, price, qty, create_date, modify_date, " +
                            "last_modify_user) VALUES (?,?,(SELECT price FROM product WHERE id=?),?,?,?,?)",
                    returnIdResultSetHandler, order.getId(), idProduct, idProduct, qty, MyTime.now(),
                    MyTime.now(), user.getId());
            connection.commit();
            LOGGER.info("Add productId " + idProduct + " qty:" + qty + " in orderItem: " + insert);
            return insert;
        } catch (SQLException e) {
            throw ExceptionFactory.getInternalServerErrorException("Can`t execute SQL request: " + e.getMessage(), e);
        }
    }

    @Override
    public OrderItem modifyProductQtyInOrderItem(Order order, User user, int idProduct, int qty) {
        try (Connection connection = dataSource.getConnection()) {
            List<OrderItem> update = JDBCUtils.update(connection,
                    "UPDATE order_item SET price=(SELECT price FROM product WHERE id=?),qty=?, " +
                            "modify_date=?, last_modify_user=? WHERE id_order=? AND id_product=?;",
                    orderItemListResultSetHandler, idProduct, qty, MyTime.now(), user.getId(),
                    order.getId(), idProduct);
            LOGGER.info("Change qty productId: " + idProduct + " ,  " + qty + " pieces in order: " + order.getId());
            return update.get(0);
        } catch (SQLException e) {
            throw ExceptionFactory.getInternalServerErrorException("Can`t execute SQL request: " + e.getMessage(), e);
        }
    }

    @Override
    public void deleteOrderItem(Order order, int idProduct, User user) {
        try (Connection connection = dataSource.getConnection()) {
            JDBCUtils.update(connection,
                    "DELETE FROM order_item WHERE id_order=? AND id_product=?;", orderItemListResultSetHandler,
                    order.getId(), idProduct);
            LOGGER.info("Remove from order " + order.getId() + ", product: " + idProduct + "  by userId: " + user.getId());
            connection.commit();
        } catch (SQLException e) {
            throw ExceptionFactory.getInternalServerErrorException("Can`t execute SQL request: " + e.getMessage(), e);
        }
    }

    public List<OrderItem> getListOrderItem(int idOrderCart) {
        try (Connection connection = dataSource.getConnection()) {
            return JDBCUtils.select(connection, "SELECT * FROM order_item WHERE id_order = ?;",
                    orderItemListResultSetHandler, idOrderCart);
        } catch (SQLException e) {
            throw ExceptionFactory.getInternalServerErrorException("Can`t execute SQL request: " + e.getMessage(), e);
        }
    }

    public void refreshOrderPrice(int idOrder) {
        try (Connection connection = dataSource.getConnection()) {
            JDBCUtils.update(connection,
                    "UPDATE order_user SET price = (SELECT SUM(price*qty) FROM order_item WHERE id_order=?) WHERE id=?;",
                    orderListResultSetHandler, idOrder, idOrder);
            connection.commit();
        } catch (SQLException e) {
            throw ExceptionFactory.getInternalServerErrorException("Can`t execute SQL request: " + e.getMessage(), e);
        }
    }

    @Override
    public void confirmOrder(User user, int idOrder) {
        try (Connection connection = dataSource.getConnection()) {
            JDBCUtils.update(connection,
                    "UPDATE order_user SET order_status=? WHERE id_user=? AND id=?;",
                    orderListResultSetHandler, OrderStatus.WAITING_TO_BE_CHECKED.getValue(), user.getId(), idOrder);

            connection.commit();
        } catch (SQLException e) {
            throw ExceptionFactory.getInternalServerErrorException("Can`t execute SQL request: " + e.getMessage(), e);
        }
    }


}
