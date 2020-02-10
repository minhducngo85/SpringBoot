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

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;

/**
 * 
 * @author Minh Duc Ngo
 *
 * @param <T>
 *            the type of Entity object
 * @param <ID>
 *            the Id type
 */
@Repository
public abstract class AbstractDao<T, ID extends Serializable> {

    /** The Hibernate session factory */
    @Autowired
    protected SessionFactory sessionFactory;

    /**
     * To save object of type T
     * 
     * @return the object id
     */
    @SuppressWarnings("unchecked")
    @Transactional
    public ID save(T object) {
	Session session = this.sessionFactory.getCurrentSession();
	return (ID) session.save(object);
    }

    /**
     * to find object by id
     */
    @Transactional
    public T findById(ID id) {
	return (T) sessionFactory.getCurrentSession().get(genericType(), id);
    }

    /**
     * delete a table row by id
     * 
     * @param id
     * @return
     */
    @Transactional
    public boolean deleteById(ID id) {
	Object persistentInstance = sessionFactory.getCurrentSession().get(genericType(), id);
	if (persistentInstance != null) {
	    sessionFactory.getCurrentSession().delete(persistentInstance);
	    return true;
	}
	return false;
    }

    /**
     * to find all table rows
     * 
     * @param orderby
     * @param order
     * @return
     */
    @Transactional
    public List<T> findAll(String orderby, String order) {
	CriteriaBuilder cBuilder = sessionFactory.getCurrentSession().getCriteriaBuilder();
	CriteriaQuery<T> cQuery = cBuilder.createQuery(genericType());
	Root<T> root = (Root<T>) cQuery.from(genericType());
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

    /**
     * find a table row for the given property
     * 
     * @param attribute
     * @param value
     * @return
     */
    @Transactional
    public T findByAttribute(String attribute, Object value) {
	CriteriaBuilder cBuilder = sessionFactory.getCurrentSession().getCriteriaBuilder();
	CriteriaQuery<T> cQuery = (CriteriaQuery<T>) cBuilder.createQuery(genericType());
	Root<T> root = (Root<T>) cQuery.from(genericType());
	cQuery.where(cBuilder.equal(root.get(attribute), value));
	try {
	    return sessionFactory.getCurrentSession().createQuery(cQuery).getSingleResult();
	} catch (NoResultException e) {
	    return null;
	}
    }

    /**
     * 
     * @return the class name of generic type T
     */
    @SuppressWarnings("unchecked")
    public Class<T> genericType() {
	return (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }
}
