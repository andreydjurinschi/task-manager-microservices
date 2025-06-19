package com.example.userservice.dao;

import com.example.userservice.entity.User;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UserDAOImpl implements UserDAO {

    private final SessionFactory sessionFactory;

    @Autowired
    public UserDAOImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public List<User> findAll() {
        try(var session = sessionFactory.openSession()){
            return session.createQuery("from User", User.class).list();
        }
    }

    @Override
    public User findById(long id) {
        try(var session = sessionFactory.openSession()) {
            String sql = "from User where id = :id";
            return session.createQuery(sql, User.class).setParameter("id", id).uniqueResult();
        }
    }

    @Override
    public User findByUsername(String username) {
        try(var session = sessionFactory.openSession()){
            String sql = "from User u where u.username = :username";
            return session.createQuery(sql, User.class).setParameter("username", username).uniqueResult();
        }
    }

    @Override
    public void createOrUpdate(User user) {
        try (var session = sessionFactory.openSession()) {
            var transaction = session.beginTransaction();

            try {
                if (user.getId() == null) {
                    session.persist(user);
                } else {
                    session.merge(user);
                }
                transaction.commit();
            } catch (Exception e) {
                if (transaction != null && transaction.isActive()) {
                    transaction.rollback();
                }
                throw e;
            }
        }
    }

    @Override
    public void delete(Long Id) {
        var session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        try(session) {
            User user = findById(Id);
            session.remove(user);
            transaction.commit();
        }catch(Exception e){
            transaction.rollback();
        }
    }
}
