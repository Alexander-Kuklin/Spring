package com.epam.spring.service;

import com.epam.spring.entity.Product;
import com.epam.spring.entity.ProductCategory;
import com.epam.spring.entity.User;

import java.util.List;

public interface ProductService {

    Product addProduct(int idCategory, String alias, String title, String textShort, String textFull,
                       double price, int qty, User user);

    Product getProductById(int productId);

    List<Product> getListProduct();

    List<ProductCategory> getListProductCategory();

    ProductCategory addProductCategory(int idParent, String alias, String title, String text, User user);
}
