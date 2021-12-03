package fun.onysakura.algorithm.kits.single.file.text.generator.database.model;

import lombok.Data;

import java.util.List;

@Data
public class TableInfo {
    private String schema;
    private String tableName;
    private String tableComment;
    private String className;
    private List<ColumnInfo> columns;
}