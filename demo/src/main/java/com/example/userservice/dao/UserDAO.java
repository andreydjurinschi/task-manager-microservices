package com.example.userservice.dao;

import com.example.userservice.entity.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.util.List;


public interface UserDAO {
    List<User> findAll();
    User findById(long id);
    User findByUsername(String username);
    void createOrUpdate(User user);
    void delete(Long Id);
}
