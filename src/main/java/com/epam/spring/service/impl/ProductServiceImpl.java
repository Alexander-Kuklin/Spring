package com.epam.spring.service.impl;

import com.epam.spring.dao.ProductRepository;
import com.epam.spring.entity.Product;
import com.epam.spring.entity.ProductCategory;
import com.epam.spring.entity.User;
import com.epam.spring.parsers.ParseProductToDB;
import com.epam.spring.service.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class ProductServiceImpl implements ProductService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ParseProductToDB.class);

    @Autowired
    private ProductRepository productRepository;

    public Product addProduct(int idCategory, String alias, String title, String textShort, String textFull,
                              double price, int qty, User user) throws IllegalArgumentException {
        Product newProduct = new Product();
        newProduct.setIdCategory(idCategory);
        newProduct.setAlias(alias);
        newProduct.setTitle(title);
        newProduct.setTextShort(textShort);
        newProduct.setTextFull(textFull);
        newProduct.setPrice(price);
        newProduct.setQty(qty);
        newProduct.setLastModifyUser(user.getId());

        productRepository.addProduct(newProduct);
        return productRepository.getProductById(newProduct.getId());
    }

    public List<Product> getListProduct() {
        return productRepository.getListProduct();
    }

    public Product getProductById(int productId) {
        return productRepository.getProductById(productId);
    }

    public List<ProductCategory> getListProductCategory() {
        return productRepository.getListProductCategory();
    }

    public ProductCategory addProductCategory(int idParent, String alias, String title, String text, User user) {
        ProductCategory newProductCategory = new ProductCategory();
        newProductCategory.setIdParent(idParent);
        newProductCategory.setAlias(alias);
        newProductCategory.setTitle(title);
        newProductCategory.setText(text);
        newProductCategory.setLastModifyUser(user.getId());

        productRepository.addProductCategory(newProductCategory);
        return productRepository.getProductCategoryById(newProductCategory.getId());
    }

}
