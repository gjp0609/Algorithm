package fun.onysakura.algorithm.spring.database.transaction;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionTestRepository extends JpaRepository<TransactionTestModel, Long> {

}
