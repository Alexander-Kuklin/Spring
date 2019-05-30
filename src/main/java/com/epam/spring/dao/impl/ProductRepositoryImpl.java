package com.epam.spring.dao.impl;

import com.epam.spring.dao.ProductRepository;
import com.epam.spring.entity.Product;
import com.epam.spring.entity.ProductCategory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.util.List;

public class ProductRepositoryImpl implements ProductRepository {

    @Autowired
    private EntityManagerFactory entityManagerFactory;

    @Override
    public List<Product> getListProduct() {
        EntityManager em = entityManagerFactory.createEntityManager();
        List<Product> productList = em.createQuery("from Product", Product.class).getResultList();
        em.close();
        return productList;
    }

    @Override
    public List<Product> getListProductByCategory(int idCategory) {
        EntityManager em = entityManagerFactory.createEntityManager();
        List<Product> productList = em.createQuery("from Product where category.id = :idCategory", Product.class)
                .setParameter("idCategory", idCategory)
                .getResultList();
        em.close();
        return productList;
    }

    @Override
    public List<ProductCategory> getListProductCategory() {
        EntityManager em = entityManagerFactory.createEntityManager();
        List<ProductCategory> productCategoryList = em.createQuery("from ProductCategory", ProductCategory.class)
                .getResultList();
        em.close();
        return productCategoryList;
    }

    @Override
    public Product getProductById(int id) {
        EntityManager em = entityManagerFactory.createEntityManager();
        Product product = em.find(Product.class, id);
        em.close();
        return product;
    }

    @Override
    public ProductCategory getProductCategoryById(int id) {
        EntityManager em = entityManagerFactory.createEntityManager();
        ProductCategory productCategory = em.find(ProductCategory.class, id);
        em.close();
        return productCategory;
    }

    @Override
    public void addProductCategory(ProductCategory productCategory) {
        EntityManager em = entityManagerFactory.createEntityManager();
        em.persist(productCategory);
        em.close();
    }

    @Override
    public void addProduct(Product product) {
        EntityManager em = entityManagerFactory.createEntityManager();
        em.getTransaction().begin();
        em.persist(product);
        em.close();
    }
}
