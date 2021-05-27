package com.onysakura.algorithm.utilities.db;

import com.onysakura.algorithm.utilities.db.anno.ColumnName;
import com.onysakura.algorithm.utilities.db.anno.TableName;
import lombok.Data;

@Data
@TableName("user")
public class User {
    @ColumnName("Host")
    private String host;
    @ColumnName("User")
    private String username;
}
