package com.epam.spring.service;

import com.epam.spring.entity.Product;
import com.epam.spring.entity.ProductCategory;
import com.epam.spring.entity.User;

import java.util.List;

public interface ProductService {

    List<Product> getListProduct();

    List<Product> getListProductByCategory(int idCategory);

    List<ProductCategory> getListProductCategory();

    Product getProductById(int id);

    ProductCategory getProductCategoryById(int id);

    int addProductCategory(int idParent, String alias, String title, String text, User user);

    int addProduct(int idCategory, String alias, String title, String textShort, String textFull,
                   double price, int qty, User user);
}
