package com.epam.spring.service.impl;

import com.epam.spring.JDBC.JDBCUtils;
import com.epam.spring.entity.Product;
import com.epam.spring.entity.ProductCategory;
import com.epam.spring.entity.User;
import com.epam.spring.exception.InternalServerErrorException;
import com.epam.spring.service.ProductService;
import org.apache.commons.dbcp2.BasicDataSource;
import org.hamcrest.core.Is;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest(JDBCUtils.class)
public class ProductServiceTest {

    private ProductService productService;

    @Mock
    private BasicDataSource dataSource;

    @Mock
    private Connection connection;

    @Before
    public void setUp() throws SQLException {
        when(dataSource.getConnection()).thenReturn(connection);
        productService = new ProductServiceImpl(dataSource);
    }

    @Test (expected = RuntimeException.class)
    public void ShouldThrowExceptionWhenAddProductAlreadyExist() throws SQLException{
        Product alreadyExistProduct = new Product();
        PowerMockito.mockStatic(JDBCUtils.class);

        when(JDBCUtils.select(eq(connection), anyString(), any(), eq("title"), eq("textShort")))
                .thenReturn(alreadyExistProduct);

        productService.addProduct(3, "alias", "title", "textShort",
                "textFull", 9.99, 1, null);
    }

    @Test (expected = InternalServerErrorException.class)
    public void ShouldThrowExceptionWhenAddProductIfSqlRequestWrong() throws SQLException{
        PowerMockito.mockStatic(JDBCUtils.class);

        when(JDBCUtils.select(eq(connection), anyString(), any(), eq("title"), eq("textShort")))
                .thenThrow(new InternalServerErrorException("Can`t execute SQL request: UTest"));

        productService.addProduct(3, "alias", "title", "textShort",
                "textFull", 9.99, 1, null);
    }

    @Test
    public void ShouldReturnIdNewProduct() throws SQLException {
        User testUser = new User();
        testUser.setId(3);
        PowerMockito.mockStatic(JDBCUtils.class);

        when(JDBCUtils.select(eq(connection), anyString(), any(), eq("title"), eq("textShort")))
                .thenReturn(null);

        when(JDBCUtils.insert(eq(connection), anyString(), any(), eq(3), eq("alias"), eq("title"),
                eq("textShort"), eq("textFull"), eq(9.99), eq(1), eq(0), eq(true),
                any(), any(), eq(testUser.getId()))).thenReturn(5);
        int actual = productService.addProduct(3, "alias", "title", "textShort",
                "textFull", 9.99, 1, testUser);
        Assert.assertThat(actual, Is.is(5));
    }

    @Test
    public void ShouldReturnListProduct() throws SQLException {
        List<Product> expected = new ArrayList<>();

        PowerMockito.mockStatic(JDBCUtils.class);
        when(JDBCUtils.select(eq(connection), anyString(), any())).thenReturn(expected);

        List<Product> actual = productService.getListProduct();
        Assert.assertThat(actual, Is.is(expected));
    }

    @Test
    public void ShouldReturnListProductByCategoryId() throws SQLException{
        List<Product> expected = new ArrayList<>();

        PowerMockito.mockStatic(JDBCUtils.class);
        when(JDBCUtils.select(eq(connection), anyString(), any(), eq(4))).thenReturn(expected);

        List<Product> actual = productService.getListProductByCategory(4);
        Assert.assertThat(actual, Is.is(expected));
    }

    @Test
    public void ShouldReturnListProductCategory() throws SQLException{
        List<ProductCategory> expected = new ArrayList<>();

        PowerMockito.mockStatic(JDBCUtils.class);
        when(JDBCUtils.select(eq(connection), anyString(), any())).thenReturn(expected);

        List<ProductCategory> actual = productService.getListProductCategory();
        Assert.assertThat(actual, Is.is(expected));
    }

    @Test
    public void ShouldReturnProductById() throws SQLException{
        Product expected = new Product();

        PowerMockito.mockStatic(JDBCUtils.class);
        when(JDBCUtils.select(eq(connection), anyString(), any(), eq(5))).thenReturn(expected);

        Product actual = productService.getProductById(5);
        Assert.assertThat(actual, Is.is(expected));
    }

    @Test
    public void ShouldReturnProductCategoryById() throws SQLException{
        ProductCategory expected = new ProductCategory();

        PowerMockito.mockStatic(JDBCUtils.class);
        when(JDBCUtils.select(eq(connection), anyString(), any(), eq(3))).thenReturn(expected);

        ProductCategory actual = productService.getProductCategoryById(3);
        Assert.assertThat(actual, Is.is(expected));
    }

    @Test
    public void ShouldAddProductCategoryAndReturnId() throws SQLException {
        User user = new User();
        user.setId(2);

        PowerMockito.mockStatic(JDBCUtils.class);
        when(JDBCUtils.insert(eq(connection), anyString(), any(), eq(3), eq("Alias"),
                eq("Title"), eq("Text"), eq(true), any(), any(), eq(2))).thenReturn(3);

        int result = productService.addProductCategory(3, "Alias", "Title", "Text", user);
        Assert.assertThat(result, Is.is(3));

    }
}