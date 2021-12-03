package fun.onysakura.algorithm.utils.db.mysql;

import lombok.Data;

@Data
public class FieldDef {
    private String name;
    private Class<?> type;
    private String columnName;
}
