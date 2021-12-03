package fun.onysakura.algorithm.spring.jpa.insert;

import lombok.Data;
import org.springframework.data.domain.Persistable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "spring_test_insert_snowflake")
public class JpaInsertModalSnowflake implements Persistable<String> {

    @Id
    @Column(name = "id")
    private String id;
    @Column(name = "name")
    private String name;

    public JpaInsertModalSnowflake() {
    }

    public JpaInsertModalSnowflake(String id, String name) {
        this.id = id;
        this.name = name;
    }


    @Override
    public boolean isNew() {
        return true;
    }
}
