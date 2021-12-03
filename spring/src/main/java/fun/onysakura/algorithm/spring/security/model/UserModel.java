package fun.onysakura.algorithm.spring.security.model;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@Table(name = "spring_security_user")
public class UserModel implements UserDetails {

    @Id
    @GeneratedValue(generator = "idGenerator")
    @GenericGenerator(name = "idGenerator", strategy = "fun.onysakura.algorithm.spring.jpa.id.IdGenerator")
    @Column(columnDefinition = "bigint(20) comment 'id'")
    private Long id;

    @Column(columnDefinition = "varchar(50) comment '用户名'")
    private String username;

    @Column(columnDefinition = "varchar(100) comment '用户密码'")
    private String password;

    @Column(columnDefinition = "boolean comment '是否启用'")
    private Boolean enabled;

    @Column(columnDefinition = "datetime comment '密码修改日期'")
    private Date lastPasswordResetDate;

    @Transient
    private transient Set<GrantedAuthority> authorities = new HashSet<>(1);

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return this.enabled;
    }
}
