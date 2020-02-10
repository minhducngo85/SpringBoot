package com.minhduc.tuto.springboot.hibernate.dao;

import java.util.List;

import javax.persistence.NoResultException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class CommonDao {

    @Autowired
    protected SessionFactory sessionFactory;

    @Transactional
    <T> long save(T object) {
	Session session = this.sessionFactory.getCurrentSession();
	return (Long) session.save(object);
    }

    @Transactional
    <T> T findById(Class<T> type, long id) {
	return (T) sessionFactory.getCurrentSession().get(type, id);
    }

    @Transactional
    boolean deleteById(Class<?> type, long id) {
	Object persistentInstance = sessionFactory.getCurrentSession().get(type, id);
	if (persistentInstance != null) {
	    sessionFactory.getCurrentSession().delete(persistentInstance);
	    return true;
	}
	return false;
    }

    @Transactional
    <T> List<T> findAll(Class<T> type, String orderby, String order) {
	CriteriaBuilder cBuilder = sessionFactory.getCurrentSession().getCriteriaBuilder();
	CriteriaQuery<T> cQuery = cBuilder.createQuery(type);
	Root<T> root = cQuery.from(type);
	if (orderby != null) {
	    if ("ASC".equalsIgnoreCase(order)) {
		cQuery.orderBy(cBuilder.asc(root.get(orderby)));
	    } else {
		cQuery.orderBy(cBuilder.desc(root.get(orderby)));
	    }
	}
	List<T> results = sessionFactory.getCurrentSession().createQuery(cQuery).getResultList();
	return results;
    }

    @Transactional
    <T> T findByAttribute(Class<T> type, String attribute, Object value) {
	CriteriaBuilder cBuilder = sessionFactory.getCurrentSession().getCriteriaBuilder();
	CriteriaQuery<T> cQuery = cBuilder.createQuery(type);
	Root<T> root = cQuery.from(type);
	cQuery.where(cBuilder.equal(root.get(attribute), value));
	try {
	    return sessionFactory.getCurrentSession().createQuery(cQuery).getSingleResult();
	} catch (NoResultException e) {
	    return null;
	}
    }
}
