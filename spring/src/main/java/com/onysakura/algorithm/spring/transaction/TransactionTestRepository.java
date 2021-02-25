package com.onysakura.algorithm.spring.transaction;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionTestRepository extends JpaRepository<TransactionTestModel, Long> {

}
