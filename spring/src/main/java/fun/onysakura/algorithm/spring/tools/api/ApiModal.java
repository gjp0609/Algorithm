package fun.onysakura.algorithm.spring.tools.api;

import lombok.Data;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@ToString
@Entity
@Table(name = "spring_tools_api")
public class ApiModal {

    @Id
    @Column(columnDefinition = "varchar(10)")
    private String code;

    @Column(columnDefinition = "text")
    private String content;
}
