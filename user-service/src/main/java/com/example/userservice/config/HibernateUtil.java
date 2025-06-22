package com.example.userservice.config;

import com.example.userservice.entity.User;
import com.example.userservice.entity.User;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration; // <-- важно: это Hibernate
import org.springframework.context.annotation.Bean;

import java.util.Properties;

@org.springframework.context.annotation.Configuration
public class HibernateUtil {

    @Bean
    public SessionFactory sessionFactory() {
        Properties props = new Properties();


        String url = System.getenv("SPRING_DATASOURCE_URL");
        String username = System.getenv("SPRING_DATASOURCE_USERNAME");
        String password = System.getenv("SPRING_DATASOURCE_PASSWORD");


        if (url == null || url.isEmpty()) {
            url = "jdbc:postgresql://localhost:5433/task-manager-user_db";
            username = "admin";
            password = "pass";
        }

        props.put("hibernate.connection.url", url);
        props.put("hibernate.connection.username", username);
        props.put("hibernate.connection.password", password);
        props.put("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");
        props.put("hibernate.hbm2ddl.auto", "update");
        props.put("hibernate.show_sql", "true");


        Configuration configuration = new Configuration();
        configuration.setProperties(props);


        configuration.addAnnotatedClass(User.class);

        return configuration.buildSessionFactory();
    }
}
