package com.onysakura.algorithm.spring.database.index;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class TestIndexModel {
    private Long id;
    private String str;
    private String date;
    private Type type;
    private Integer num;
    private String text;
}

enum Type {
    A,
    B
}
