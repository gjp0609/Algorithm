package com.onysakura.algorithm.file.text.generator.database.model;

import lombok.Data;

@Data
public class ColumnInfo {
    private String comment;
    private String columnName;
    private String name;
    private String dataType;
    private String type;
    private String length;
    private String lengthValid;
    private String nullable;
    private String nullableValid;
}