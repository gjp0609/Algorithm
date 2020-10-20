package com.onysakura.algorithm.spring.transaction;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Table(name = "transaction_test")
public class TransactionTestModel {

    @Id
    @GeneratedValue(generator = "idGenerator")
    @GenericGenerator(name = "idGenerator", strategy = "com.onysakura.algorithm.spring.jpa.id.IdGenerator")
    @Column(columnDefinition = "varchar(20)")
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

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public Long getTestNo() {
        return testNo;
    }

    public void setTestNo(Long testNo) {
        this.testNo = testNo;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return "TransactionTestModel{" +
                "id='" + id + '\'' +
                ", text='" + text + '\'' +
                '}';
    }
}
