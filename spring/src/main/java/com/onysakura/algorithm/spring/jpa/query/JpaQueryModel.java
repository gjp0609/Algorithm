package com.onysakura.algorithm.spring.jpa.query;

import lombok.Data;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

@Data
@ToString
@Entity
@Table(name = "spring_test_jpa_query", indexes = {@Index(columnList = "date")})
public class JpaQueryModel {

    @Id
    @GeneratedValue(generator = "idGenerator")
    @GenericGenerator(name = "idGenerator", strategy = "com.onysakura.algorithm.spring.jpa.id.IdGenerator")
    private Long id;
    @Column(columnDefinition = "varchar(200)")
    private String str;
    @Column(columnDefinition = "datetime")
    private Date date;
    @Column(columnDefinition = "enum('A','B')")
    @Enumerated(EnumType.STRING)
    private Type type;
    @Column(columnDefinition = "tinyint(1)")
    private Integer num;
    @Column(columnDefinition = "text")
    private String text;
}
