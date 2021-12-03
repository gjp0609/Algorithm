package fun.onysakura.algorithm.kits.single.file.text.generator.database.model;

import fun.onysakura.algorithm.utils.db.anno.ColumnName;
import fun.onysakura.algorithm.utils.db.anno.TableName;
import lombok.Data;

@Data
@TableName("COLUMNS")
public class Column {
    @ColumnName("TABLE_SCHEMA")
    private String tableSchema;
    @ColumnName("TABLE_NAME")
    private String tableName;
    @ColumnName("COLUMN_NAME")
    private String columnName;
    @ColumnName("ORDINAL_POSITION")
    private Long ordinalPosition;
    @ColumnName("COLUMN_DEFAULT")
    private String columnDefault;
    @ColumnName("IS_NULLABLE")
    private String isNullable;
    @ColumnName("DATA_TYPE")
    private String dataType;
    @ColumnName("CHARACTER_MAXIMUM_LENGTH")
    private String characterMaximumLength;
    @ColumnName("NUMERIC_PRECISION")
    private String numericPrecision;
    @ColumnName("NUMERIC_SCALE")
    private String numericScale;
    @ColumnName("COLUMN_KEY")
    private String columnKey;
    @ColumnName("COLUMN_COMMENT")
    private String columnComment;
}