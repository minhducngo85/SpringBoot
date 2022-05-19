package com.minhduc.tuto.springboot.primefaces.demo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * JpaRepository extends PagingAndSortingRepository which in turn extends
 * CrudRepository.
 * 
 * Their main functions are:
 * 
 * CrudRepository mainly provides CRUD functions.<br/>
 * 
 * PagingAndSortingRepository provides methods to do pagination and sorting
 * records.<br/>
 * 
 * JpaRepository provides some JPA-related methods such as flushing the
 * persistence context and deleting records in a batch.
 * 
 * @author Berry
 *
 */
@Repository
public interface CarRepository extends JpaRepository<Car, Long> {
}
