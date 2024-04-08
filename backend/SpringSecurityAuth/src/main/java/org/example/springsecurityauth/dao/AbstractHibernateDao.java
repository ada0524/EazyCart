package org.example.springsecurityauth.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class AbstractHibernateDao<T> {

    @Autowired
    protected SessionFactory sessionFactory;

    protected Session getCurrentSession() {
        return sessionFactory.getCurrentSession();
    }

    protected Class<T> clazz;

    protected final void setClazz(final Class<T> clazzToSet) {
        clazz = clazzToSet;
    }

    public void save(T item) {
        getCurrentSession().save(item);
    }
}


