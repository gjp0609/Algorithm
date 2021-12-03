package fun.onysakura.algorithm.kits.single.file.text.generator.database.model;

import fun.onysakura.algorithm.utils.db.anno.ColumnName;
import fun.onysakura.algorithm.utils.db.anno.TableName;
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