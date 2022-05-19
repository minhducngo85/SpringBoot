package com.minhduc.tuto.jwt.repository;

import java.util.Optional;

import com.minhduc.tuto.jwt.model.ERole;
import com.minhduc.tuto.jwt.model.Role;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(ERole name);
}
