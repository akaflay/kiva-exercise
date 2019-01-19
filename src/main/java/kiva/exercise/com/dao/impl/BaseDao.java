package kiva.exercise.com.dao.impl;

import java.io.Serializable;
import java.util.List;

import org.hibernate.SessionFactory;
import org.hibernate.jpa.QueryHints;
import org.springframework.beans.factory.annotation.Autowired;

import static org.hibernate.jpa.QueryHints.HINT_READONLY;

public class BaseDao<T> {
    @Autowired
    private SessionFactory sessionFactory;

    @SuppressWarnings("unchecked")
    public List<T> find(Class<?> cl) {
        return sessionFactory.getCurrentSession()
                .createQuery(String.format("from %s", cl.getSimpleName()))
                .setHint(HINT_READONLY, true)
                .list();
    }

    @SuppressWarnings("unchecked")
    public T findById(Class<?> cl, Serializable id) {
        return (T) sessionFactory.getCurrentSession()
                .get(cl, id);
    }

    public List<T> findListBySecondryId(Class<?> cl, String columnName, Serializable id) {
        return sessionFactory.getCurrentSession()
                .createQuery(String.format("from %s where %s='%s'", cl.getSimpleName(), columnName, id))
                .setHint(HINT_READONLY, true)
                .list();
    }

    public void delete(Class<?> cl, String id) {
        T obj = findById(cl, id);
        sessionFactory.getCurrentSession().delete(obj);
    }

    public void add(T object) {
        sessionFactory.getCurrentSession().save(object);
    }

    public void update(T object) {
        sessionFactory.getCurrentSession().update(object);
    }

}
