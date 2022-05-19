package com.minhduc.tuto.jwt.repository;

import java.util.Optional;

import com.minhduc.tuto.jwt.model.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

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
 * @author Minh Duc
 *
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);

    Boolean existsByUsername(String username);

    Boolean existsByEmail(String email);
}
