package com.minhduc.tuto.springboot.jdbc.dao;

import org.springframework.data.repository.CrudRepository;

import com.minhduc.tuto.springboot.jdbc.model.User;

/**
 * CrudRepository provides basic data base opreations like:
 * 
 * findAll, findById, delete, save, update
 * 
 * @author Minh Duc Ngo
 *
 */
public interface UserRepository extends CrudRepository<User, Integer> {

}
