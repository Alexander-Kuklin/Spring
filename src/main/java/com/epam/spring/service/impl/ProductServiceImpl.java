package com.epam.spring.service.impl;

import com.epam.spring.JDBC.JDBCUtils;
import com.epam.spring.JDBC.ResultSetHandler;
import com.epam.spring.JDBC.ResultSetHandlerFactory;
import com.epam.spring.entity.Product;
import com.epam.spring.entity.ProductCategory;
import com.epam.spring.entity.User;
import com.epam.spring.exception.ExceptionFactory;
import com.epam.spring.service.ProductService;
import com.epam.spring.util.MyTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class ProductServiceImpl implements ProductService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ProductServiceImpl.class);

    private final ResultSetHandler<List<Product>> productListResultSetHandler =
            ResultSetHandlerFactory.getListResultSetHandler(ResultSetHandlerFactory.PRODUCT_RESULT_SET_HANDLER);
    private final ResultSetHandler<Product> productResultSetHandler =
            ResultSetHandlerFactory.getEntityResultSetHandler(ResultSetHandlerFactory.PRODUCT_RESULT_SET_HANDLER);
    private final ResultSetHandler<List<ProductCategory>> categoryListResultSetHandler =
            ResultSetHandlerFactory.getListResultSetHandler(ResultSetHandlerFactory.PRODUCT_CATEGORY_RESULT_SET_HANDLER);
    private final ResultSetHandler<ProductCategory> categoryResultSetHandler =
            ResultSetHandlerFactory.getEntityResultSetHandler(ResultSetHandlerFactory.PRODUCT_CATEGORY_RESULT_SET_HANDLER);
    private final ResultSetHandler<Integer> idResultSetHandler =
            ResultSetHandlerFactory.getEntityResultSetHandler(ResultSetHandlerFactory.GET_ID_RESULT_SET_HANDLER);


    private final DataSource dataSource;

    public ProductServiceImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public List<Product> getListProduct() {
        try (Connection connection = dataSource.getConnection()) {
            return JDBCUtils.select(connection, "SELECT * FROM product;", productListResultSetHandler);
        } catch (SQLException e) {
            throw ExceptionFactory.getInternalServerErrorException("Can`t execute SQL request: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Product> getListProductByCategory(int idCategory) {
        try (Connection connection = dataSource.getConnection()) {
            return JDBCUtils.select(connection, "SELECT * FROM product WHERE id_category = ?;",
                    productListResultSetHandler, idCategory);
        } catch (SQLException e) {
            throw ExceptionFactory.getInternalServerErrorException("Can`t execute SQL request: " + e.getMessage(), e);
        }
    }

    @Override
    public List<ProductCategory> getListProductCategory() {
        try (Connection connection = dataSource.getConnection()) {
            return JDBCUtils.select(connection, "SELECT * FROM product_category;", categoryListResultSetHandler);
        } catch (SQLException e) {
            throw ExceptionFactory.getInternalServerErrorException("Can`t execute SQL request: " + e.getMessage(), e);
        }
    }

    @Override
    public Product getProductById(int id) {
        try (Connection connection = dataSource.getConnection()) {
            return JDBCUtils.select(connection, "SELECT * FROM product WHERE id=?;", productResultSetHandler, id);
        } catch (SQLException e) {
            throw ExceptionFactory.getInternalServerErrorException("Can`t execute SQL request: " + e.getMessage(), e);
        }
    }

    public ProductCategory getProductCategoryById(int id) {
        try (Connection connection = dataSource.getConnection()) {
            return JDBCUtils.select(connection, "SELECT * FROM product_category WHERE id=?;",
                    categoryResultSetHandler, id);
        } catch (SQLException e) {
            throw ExceptionFactory.getInternalServerErrorException("Can`t execute SQL request: " + e.getMessage(), e);
        }
    }

    public int addProductCategory(int idParent, String alias, String title, String text, User user) {
        try (Connection connection = dataSource.getConnection()) {
            int resultId = JDBCUtils.insert(connection,
                    "INSERT INTO product_category (id_parent, alias, title, text, active, create_date, " +
                            "modify_date, last_modify_user) VALUES (?,?,?,?,?,?,?,?);", idResultSetHandler,
                    idParent, alias, title, text, true, MyTime.now(), MyTime.now(), user.getId());
            connection.commit();
            return resultId;
        } catch (SQLException e) {
            throw ExceptionFactory.getInternalServerErrorException("Can`t execute SQL request: " + e.getMessage(), e);
        }
    }

    public int addProduct(int idCategory, String alias, String title, String textShort, String textFull,
                          double price, int qty, User user) throws IllegalArgumentException {
        try (Connection connection = dataSource.getConnection()) {
            Product product = JDBCUtils.select(connection,
                    "SELECT * FROM product WHERE title LIKE ? AND text_Short LIKE ?",
                    productResultSetHandler, title, textShort);

            if (product != null) {
                LOGGER.info("Product with name = " + title + " already exists in the database");
                return product.getId();
                //throw ExceptionFactory.getIllegalArgumentException("Product with name = " + title + " already exists in the database");
            } else {
                Integer resultId = JDBCUtils.insert(connection,
                        "INSERT INTO \"product\" (id_category, alias, title, text_short, text_full, price, qty, discount," +
                                " active, create_date, modify_date, last_modify_user) VALUES (?,?,?,?,?,?,?,?,?,?,?,?)",
                        idResultSetHandler, idCategory, alias, title, textShort, textFull, price, qty, 0,
                        true, MyTime.now(), MyTime.now(), user.getId());

                connection.commit();
                return resultId;
            }
        } catch (SQLException e) {
            throw ExceptionFactory.getInternalServerErrorException("Can`t execute SQL request: " + e.getMessage(), e);
        }
    }
}
