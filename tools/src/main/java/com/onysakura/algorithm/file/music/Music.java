package com.onysakura.algorithm.file.music;

import com.onysakura.algorithm.utilities.db.anno.TableName;
import lombok.Data;
import lombok.ToString;

@TableName("MUSIC")
@Data
@ToString
public class Music {
    private String id;
    private String name;
    private String fullName;
    private String size;
    private FileType type;

    public void setType(String type) {
        this.type = FileType.getType(type);
    }

}
