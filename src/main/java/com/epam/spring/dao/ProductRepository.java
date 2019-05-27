package com.epam.spring.dao;

import com.epam.spring.entity.Product;
import com.epam.spring.entity.ProductCategory;

import java.util.List;

public interface ProductRepository {

    List<Product> getListProduct();

    List<Product> getListProductByCategory(int idCategory);

    List<ProductCategory> getListProductCategory();

    Product getProductById(int id);

    ProductCategory getProductCategoryById(int id);

    void addProductCategory(ProductCategory productCategory);

    void addProduct(Product product);
}
