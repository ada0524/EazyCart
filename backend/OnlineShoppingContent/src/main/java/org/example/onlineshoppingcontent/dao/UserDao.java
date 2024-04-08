package org.example.onlineshoppingcontent.dao;

import org.example.onlineshoppingcontent.model.Product;
import org.example.onlineshoppingcontent.model.User;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.Set;

@Repository
public class UserDao extends AbstractHibernateDao<User> {

    public UserDao() {
        setClazz(User.class);
    }

    public void saveUser(User user) {
        this.save(user);
    }

    public User getUserById(Long userId) {
        return this.getById(userId);
    }

    public void update(User user) {this.update(user);}

    public User getUserByUsername(String username) {
        Session session = getCurrentSession();
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<User> criteria = builder.createQuery(clazz);
        Root<User> root = criteria.from(clazz);

        criteria.select(root)
                .where(builder.equal(root.get("username"), username));

        Query<User> query = session.createQuery(criteria);
        return query.uniqueResult();
    }


}

