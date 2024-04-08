package org.example.springsecurityauth.dao;

import org.example.springsecurityauth.entity.User;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class UserDao extends AbstractHibernateDao {

    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public UserDao(BCryptPasswordEncoder passwordEncoder) {

        this.passwordEncoder = passwordEncoder;
        setClazz(User.class);
    }


    public void saveUser(User user) {
        this.save(user);
    }

    public User createUser(String username, String email, String password) {
        User user = new User(username, email, password);
        return user;
    }

    public Optional<User> getUserByUsername(String username) {
        Session session = sessionFactory.getCurrentSession();
        String hql = "FROM User WHERE username = :username";
        List<User> users = session.createQuery(hql, User.class)
                                  .setParameter("username", username)
                                  .getResultList();

        return users.isEmpty() ? Optional.empty() : Optional.of(users.get(0));

    }

    public Optional<User> getUserByEmail(String email) {
        Session session = sessionFactory.getCurrentSession();
        String hql = "FROM User WHERE email = :email";
        List<User> users = session.createQuery(hql, User.class)
                .setParameter("email", email)
                .getResultList();

        return users.isEmpty() ? Optional.empty() : Optional.of(users.get(0));
    }


}
