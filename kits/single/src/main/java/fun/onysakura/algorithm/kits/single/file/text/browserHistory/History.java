package fun.onysakura.algorithm.kits.single.file.text.browserHistory;

import fun.onysakura.algorithm.utils.db.anno.TableName;
import lombok.Data;

@Data
@TableName("history_go")
public class History {
    private String id;
    private String time;
    private String title;
    private String url;

    public History() {
    }

    public History(String time, String title, String url) {
        this.time = time;
        this.title = title;
        this.url = url;
    }
}
