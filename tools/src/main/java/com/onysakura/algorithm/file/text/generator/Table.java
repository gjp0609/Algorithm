package com.onysakura.algorithm.file.text.generator;

import com.onysakura.algorithm.utilities.db.anno.ColumnName;
import com.onysakura.algorithm.utilities.db.anno.TableName;
import lombok.Data;

@Data
@TableName("TABLES")
public class Table {
    @ColumnName("TABLE_SCHEMA")
    private String tableSchema;
    @ColumnName("TABLE_NAME")
    private String tableName;
    @ColumnName("TABLE_COMMENT")
    private String tableComment;
}