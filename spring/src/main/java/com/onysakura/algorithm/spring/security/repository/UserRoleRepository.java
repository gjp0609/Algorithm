package com.onysakura.algorithm.spring.security.repository;

import com.onysakura.algorithm.spring.security.model.UserRoleModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRoleRepository extends JpaRepository<UserRoleModel, Long> {

    List<UserRoleModel> findAllByUsername(String username);
    
    void deleteAllByUsername(String username);
}
