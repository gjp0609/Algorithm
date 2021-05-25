package com.onysakura.algorithm.utilities.db.mysql;

import lombok.Data;

@Data
public class FieldDef {
    private String name;
    private Class<?> type;
    private String columnName;
}
