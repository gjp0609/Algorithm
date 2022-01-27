package fun.onysakura.algorithm.kits.single.file.image;

import com.benjaminwan.ocrlibrary.OcrResult;
import fun.onysakura.algorithm.kits.single.Constants;
import fun.onysakura.algorithm.utils.db.anno.ColumnName;
import fun.onysakura.algorithm.utils.db.anno.TableName;
import fun.onysakura.algorithm.utils.db.sqlite.BaseRepository;
import fun.onysakura.algorithm.utils.db.sqlite.SQLite;
import lombok.Data;

import java.io.File;
import java.util.List;
import java.util.Objects;


public class TextSearchImage {

    private static final String format = ".jpg.png";
    private static final String dir = "C:/Files/Data/ScreenShot/Snipaste/auto/";
    private static final String DB_PATH = Constants.OUTPUT_PATH + "/imageText.db";
    private static final BaseRepository<ImageText> REPOSITORY;

    private static final HashSet<String> SKIP = new HashSet<>();

    static {
        SQLite.open(DB_PATH);
        REPOSITORY = new BaseRepository<>(ImageText.class);
        SKIP.add("skip.jpg");
    }

    public static void main(String[] args) throws Exception {
        check(dir);
    }

    public static void check(String dir) throws Exception {
        File dirFile = new File(dir);
        if (dirFile.exists()) {
            for (File file : Objects.requireNonNull(dirFile.listFiles())) {
                String absolutePath = file.getAbsolutePath();
                if (file.isDirectory()) {
                    check(absolutePath);
                } else {
                    String fileName = file.getName();
                    int lastIndexOf = fileName.lastIndexOf(".");
                    if (lastIndexOf > 0 && format.contains(fileName.substring(lastIndexOf)) && !SKIP.contains(fileName)) {
                        List<ImageText> list = REPOSITORY.select(new ImageText(absolutePath, null));
                        if (list.isEmpty()) {
                            OcrResult result = OCRUtils.detect(absolutePath);
                            REPOSITORY.insert(new ImageText(absolutePath, result.getStrRes()));
                        }
                    }
                }
            }
        }
    }

    @Data
    @TableName("image_text")
    public static class ImageText {
        @ColumnName("ID")
        private String id;
        @ColumnName("PATH")
        private String path;
        @ColumnName("TEXT")
        private String text;

        public ImageText() {
        }

        public ImageText(String path, String text) {
            this.path = path;
            this.text = text;
        }
    }
}
