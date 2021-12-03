package fun.onysakura.algorithm.utils.db;

import fun.onysakura.algorithm.utils.db.anno.ColumnName;
import fun.onysakura.algorithm.utils.db.anno.TableName;
import lombok.Data;

@Data
@TableName("user")
public class User {
    @ColumnName("Host")
    private String host;
    @ColumnName("User")
    private String username;
}
