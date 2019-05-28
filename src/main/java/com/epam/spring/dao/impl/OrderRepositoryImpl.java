package com.epam.spring.dao.impl;

import com.epam.spring.dao.OrderRepository;
import com.epam.spring.entity.Order;
import com.epam.spring.entity.OrderItem;
import com.epam.spring.entity.OrderStatus;
import com.epam.spring.entity.User;
import com.epam.spring.util.MyTime;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.util.List;

public class OrderRepositoryImpl implements OrderRepository {

    @Autowired
    private EntityManagerFactory entityManagerFactory;

    public List<Order> getListOrderUserWithStatus(User user, OrderStatus orderStatus) {
        EntityManager em = entityManagerFactory.createEntityManager();
        String query = "FROM Order WHERE idUser = :user AND orderStatus = :orderStatus";
        List<Order> resultList = em.createQuery(query, Order.class)
                .setParameter("user", user.getId())
                .setParameter("orderStatus", orderStatus)
                .getResultList();
        em.close();
        return resultList;
    }

    @Override
    public List<Order> getListOrderUser(User user) {
        EntityManager em = entityManagerFactory.createEntityManager();
        String query = "FROM Order WHERE idUser = :user";
        List<Order> resultList = em.createQuery(query, Order.class)
                .setParameter("user", user.getId())
                .getResultList();
        em.close();
        return resultList;
    }

    @Override
    public Order getOrder(int idOrder) {
        EntityManager em = entityManagerFactory.createEntityManager();
        Order order = em.find(Order.class, idOrder);
        em.close();
        return order;
    }

    @Override
    public OrderItem addOrderItem(OrderItem orderItem) {
        EntityManager em = entityManagerFactory.createEntityManager();
        em.getTransaction().begin();
        em.persist(orderItem);
        em.flush();
        em.getTransaction().commit();
        return orderItem;
    }

    @Override
    public Order addOrder(Order order) {
        EntityManager em = entityManagerFactory.createEntityManager();
        em.getTransaction().begin();
        em.persist(order);
        em.getTransaction().commit();
        return order;
    }

    @Override
    public void changeOrderStatus(Order order, OrderStatus orderStatus, User user) {
        EntityManager em = entityManagerFactory.createEntityManager();
        em.getTransaction().begin();
        Order managedOrder = em.find(Order.class, order.getId());
        managedOrder.setOrderStatus(orderStatus);
        managedOrder.setModifyDate(MyTime.now().toLocalDateTime());
        managedOrder.setLastModifyUser(user.getId());
        em.getTransaction().commit();
        em.close();
    }

    @Override
    public void setOrderPrice(int idOrder, double sumPrice) {
        EntityManager em = entityManagerFactory.createEntityManager();
        em.getTransaction().begin();
        em.createQuery("UPDATE Order SET price = :price WHERE id=:id")
                .setParameter("price", sumPrice)
                .setParameter("id", idOrder)
                .executeUpdate();
        em.getTransaction().commit();
        em.close();
    }
}
