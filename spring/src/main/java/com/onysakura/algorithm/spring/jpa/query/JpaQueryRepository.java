package com.onysakura.algorithm.spring.jpa.query;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface JpaQueryRepository extends JpaRepository<JpaQueryModel, Long> {

    // 不走索引
    @Query(value = "select * from spring_test_jpa_query where date like :date", nativeQuery = true)
    List<JpaQueryModel> findAllByDateLike(@Param("date") String date);

    // 走索引
    @Query(value = "select * from spring_test_jpa_query where date >= concat(:date,' 00:00:00') and date <= concat(:date, ' 23:59:59')", nativeQuery = true)
    List<JpaQueryModel> findAllByDateLikeUseIndex(@Param("date") String date);
}
