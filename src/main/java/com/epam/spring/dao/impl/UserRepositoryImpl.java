package com.epam.spring.dao.impl;

import com.epam.spring.dao.UserRepository;
import com.epam.spring.entity.User;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

public class UserRepositoryImpl implements UserRepository {

    @Autowired
    private EntityManagerFactory entityManagerFactory;

    public User getUserById(int id) {
        EntityManager em = entityManagerFactory.createEntityManager();
        User user = em.find(User.class, id);
        em.detach(user);
        em.close();
        return user;
    }

    public User validateUserPassword(String email, String password) {
        EntityManager em = entityManagerFactory.createEntityManager();
        String query = "SELECT * FROM \"user\" WHERE email = ? AND password = crypt(?, password)";
        User singleResult = (User) em.createNativeQuery(query, User.class)
                .setParameter(1, email).setParameter(2, password).getSingleResult();
        em.close();
        return singleResult;
    }

    public void createNewUser(User newUser) {
        EntityManager em = entityManagerFactory.createEntityManager();
        em.getTransaction().begin();
        em.persist(newUser);
        em.getTransaction().commit();
        em.close();
    }
}
