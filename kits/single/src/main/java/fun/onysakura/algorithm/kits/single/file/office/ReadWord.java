package fun.onysakura.algorithm.kits.single.file.office;

import lombok.extern.slf4j.Slf4j;
import org.apache.poi.xwpf.usermodel.*;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTBookmark;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTP;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

@Slf4j
public class ReadWord {


    public static void main(String[] args) throws Exception {
        readContentControl();
        readBookmark();
    }

    /**
     * 读取 Word 文档中的控件属性
     */
    public static void readContentControl() {
        try {
            XWPFDocument document = new XWPFDocument(new FileInputStream("C:\\Files\\Temp\\Types\\office\\mark.docx"));
            // returns an Iterator with paragraphs and tables
            List<IBodyElement> iBodyElements = document.getBodyElements();
            for (IBodyElement iBodyElement : iBodyElements) {
                if (iBodyElement instanceof XWPFSDT xwpfsdt) {
                    String title = xwpfsdt.getTitle();
                    ISDTContent content = xwpfsdt.getContent();
                    log.info("title: {}, content: {}", title, content);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 读取 Word 文档中的书签
     */
    public static void readBookmark() throws Exception {
        log.info("readBookmark");
        XWPFDocument doc = new XWPFDocument(new FileInputStream("C:\\Files\\Temp\\Types\\office\\mark.docx"));
        for (XWPFParagraph paragraph : doc.getParagraphs()) {
            CTP ctp = paragraph.getCTP();
            for (CTBookmark bookmark : ctp.getBookmarkStartList()) {
                String bookmarkName = bookmark.getName();
                String text = paragraph.getText();
                log.info("bookmark: {}, line: {}", bookmarkName, text);
            }
        }
    }
}
