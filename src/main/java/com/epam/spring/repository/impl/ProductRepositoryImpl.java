package com.epam.spring.repository.impl;

import com.epam.spring.entity.Product;
import com.epam.spring.entity.ProductCategory;
import com.epam.spring.entity.User;
import com.epam.spring.parsers.ParseProductToDB;
import com.epam.spring.repository.ProductRepository;
import com.epam.spring.service.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class ProductRepositoryImpl implements ProductRepository {
    private static final Logger LOGGER = LoggerFactory.getLogger(ParseProductToDB.class);

    private ProductService productService;

    public Product addProduct(int idCategory, String alias, String title, String textShort, String textFull,
                              double price, int qty, User user) throws IllegalArgumentException {
        int productId = productService.addProduct(idCategory, alias, title, textShort, textFull, price, qty, user);
        return productService.getProductById(productId);
    }

    public List<Product> getListProduct() {
        return productService.getListProduct();
    }

    public Product getProductById(int productId) {
        return productService.getProductById(productId);
    }

    public List<ProductCategory> getListProductCategory() {
        return productService.getListProductCategory();
    }

    public ProductCategory addProductCategory(int idParent, String alias, String title, String text, User user) {
        int categoryId = productService.addProductCategory(idParent, alias, title, text, user);
        return productService.getProductCategoryById(categoryId);
    }

    public void setProductService(ProductService productService) {
        this.productService = productService;
    }
}
