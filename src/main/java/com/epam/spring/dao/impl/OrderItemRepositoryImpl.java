package com.epam.spring.dao.impl;

import com.epam.spring.dao.OrderItemRepository;
import com.epam.spring.entity.Order;
import com.epam.spring.entity.OrderItem;
import com.epam.spring.entity.Product;
import com.epam.spring.entity.User;
import com.epam.spring.util.MyTime;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.Transactional;
import java.util.List;

public class OrderItemRepositoryImpl implements OrderItemRepository {

    @Autowired
    private EntityManagerFactory entityManagerFactory;

    @Override
    public double getOrderSumPrice(int idOrder) {
        EntityManager em = entityManagerFactory.createEntityManager();
        Double result = em.createQuery("SELECT SUM(price*qty) FROM OrderItem WHERE order.id = :id", Double.class)
                .setParameter("id", idOrder)
                .getSingleResult();

        if (result == null) return 0;
        return result;
    }

    @Deprecated
    @Transactional(Transactional.TxType.REQUIRED)
    public OrderItem modifyProductQtyInOrderItem(OrderItem orderItem, User user, int qty) {
        EntityManager em = entityManagerFactory.createEntityManager();

        OrderItem managedOrderItem = em.find(OrderItem.class, orderItem.getId());
        managedOrderItem.setQty(qty);
        managedOrderItem.setLastModifyUser(user.getId());
        managedOrderItem.setModifyDate(MyTime.now().toLocalDateTime());

        return managedOrderItem;
    }

    public <T> T mergeEntity(T entity) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();
        T mergeEntity = entityManager.merge(entity);
        entityManager.getTransaction().commit();
        return mergeEntity;
    }

    @Override
    public List<OrderItem> getListOrderItem(Order orderCart) {
        EntityManager em = entityManagerFactory.createEntityManager();
        return em.createQuery("from OrderItem where order = :idOrder", OrderItem.class)
                .setParameter("idOrder", orderCart)
                .getResultList();
    }

    @Override
    @Transactional
    public void deleteOrderItem(OrderItem orderItem, User user) {
        EntityManager em = entityManagerFactory.createEntityManager();

        em.getTransaction().begin();
        em.createQuery("DELETE FROM OrderItem AS oi WHERE oi.id = :id")
                .setParameter("id", orderItem.getId())
                .executeUpdate();
        em.getTransaction().commit();
    }

    @Override
    @Transactional
    public void setOrderItemPrice(OrderItem orderItem, Product product) {
        EntityManager em = entityManagerFactory.createEntityManager();
        OrderItem managedOrderItem = em.merge(orderItem);
        managedOrderItem.setPrice(product.getPrice());
    }
}
