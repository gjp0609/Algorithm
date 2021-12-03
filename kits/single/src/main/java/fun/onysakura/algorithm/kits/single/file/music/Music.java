package fun.onysakura.algorithm.kits.single.file.music;

import fun.onysakura.algorithm.utils.db.anno.TableName;
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
