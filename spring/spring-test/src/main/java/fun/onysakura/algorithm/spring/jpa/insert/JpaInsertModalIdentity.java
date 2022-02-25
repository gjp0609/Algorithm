package fun.onysakura.algorithm.spring.jpa.insert;

import lombok.Data;
import org.springframework.data.domain.Persistable;

import javax.persistence.*;

@Data
@Entity
@Table(name = "spring_test_insert_identity")
public class JpaInsertModalIdentity implements Persistable<Long> {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "name")
    private String name;

    public JpaInsertModalIdentity() {
    }

    public JpaInsertModalIdentity(String name) {
        this.name = name;
    }

    @Override
    public boolean isNew() {
        return true;
    }
}
