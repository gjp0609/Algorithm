package com.onysakura.algorithm.spring.security.model;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
@Table(name = "spring_security_user_role")
public class UserRoleModel implements Serializable {

    @Id
    @GeneratedValue(generator = "idGenerator")
    @GenericGenerator(name = "idGenerator", strategy = "com.onysakura.algorithm.spring.jpa.id.IdGenerator")
    @Column(columnDefinition = "bigint(20) comment 'id'")
    private Long id;

    @Column(columnDefinition = "varchar(50) comment '用户名'")
    private String username;

    @Column(columnDefinition = "varchar(50) comment '权限'")
    private String role;
}
