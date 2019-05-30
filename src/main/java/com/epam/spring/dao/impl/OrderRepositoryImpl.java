package com.epam.spring.dao.impl;

import com.epam.spring.dao.OrderRepository;
import com.epam.spring.entity.Order;
import com.epam.spring.entity.OrderStatus;
import com.epam.spring.entity.User;
import com.epam.spring.util.MyTime;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.Transactional;
import java.util.List;

public class OrderRepositoryImpl implements OrderRepository {

    @Autowired
    private EntityManagerFactory entityManagerFactory;

    public List<Order> getListOrderUserWithStatus(User user, OrderStatus orderStatus) {
        EntityManager em = entityManagerFactory.createEntityManager();
        String query = "FROM Order WHERE user = :user AND orderStatus = :orderStatus";
        return em.createQuery(query, Order.class)
                .setParameter("user", user)
                .setParameter("orderStatus", orderStatus.getValue())
                .getResultList();
    }

    @Override
    public List<Order> getListOrderUser(User user) {
        EntityManager em = entityManagerFactory.createEntityManager();
        String query = "FROM Order WHERE user.id = :user";
        return em.createQuery(query, Order.class)
                .setParameter("user", 2)
                .getResultList();
    }

    @Override
    public Order getOrder(int idOrder) {
        EntityManager em = entityManagerFactory.createEntityManager();
        return em.find(Order.class, idOrder);
    }

    @Override
    @Transactional
    public <T> T addEntity(T orderItem) {
        EntityManager em = entityManagerFactory.createEntityManager();
        em.getTransaction().begin();
        em.persist(orderItem);
        em.getTransaction().commit();
        return orderItem;
    }

    @Override
    @Transactional
    public void changeOrderStatus(Order order, OrderStatus orderStatus, User user) {
        EntityManager em = entityManagerFactory.createEntityManager();
        em.getTransaction().begin();
        Order managedOrder = em.find(Order.class, order.getId());
        managedOrder.setOrderStatus(orderStatus.getValue());
        managedOrder.setModifyDate(MyTime.now().toLocalDateTime());
        managedOrder.setLastModifyUser(user.getId());
        em.getTransaction().commit();
    }

    @Override
    @Transactional
    public void setOrderPrice(int idOrder, double sumPrice) {
        EntityManager em = entityManagerFactory.createEntityManager();
        em.getTransaction().begin();
        em.createQuery("UPDATE Order SET price = :price WHERE id=:id")
                .setParameter("price", sumPrice)
                .setParameter("id", idOrder)
                .executeUpdate();
        em.getTransaction().commit();
    }

    @Override
    public List<Order> getListOrderUserById(int id) {
        EntityManager em = entityManagerFactory.createEntityManager();
        return em.createQuery("FROM Order WHERE user.id = :id", Order.class)
                .setParameter("id", id)
                .getResultList();
    }
}
