package com.onysakura.algorithm.spring.tools.api;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ApiRepository extends JpaRepository<ApiModal, String> {

    
    Optional<ApiModal> findByContent(String content);
}
