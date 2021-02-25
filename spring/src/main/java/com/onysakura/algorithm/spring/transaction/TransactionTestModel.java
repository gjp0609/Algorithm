package com.onysakura.algorithm.spring.transaction;

import lombok.Data;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Data
@ToString
@Entity
@Table(name = "spring_transaction_test")
public class TransactionTestModel {

    @Id
    @GeneratedValue(generator = "idGenerator")
    @GenericGenerator(name = "idGenerator", strategy = "com.onysakura.algorithm.spring.jpa.id.IdGenerator")
    @Column(columnDefinition = "bigint(20)")
    private Long id;

    @Column(columnDefinition = "bigint(20)")
    private Long testNo;

    @Column(columnDefinition = "varchar(100)")
    private String text;

    public TransactionTestModel() {
    }

    public TransactionTestModel(Long testNo, String text) {
        this.testNo = testNo;
        this.text = text;
    }
}
