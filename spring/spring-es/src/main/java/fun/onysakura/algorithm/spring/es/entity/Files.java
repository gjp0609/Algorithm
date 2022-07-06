package fun.onysakura.algorithm.spring.es.entity;

import lombok.Data;

import java.util.Date;

@Data
public class Files {
    private String id;
    private String name;
    private String path;
    private Integer type;
    private Long size;
    private Date date;
}
