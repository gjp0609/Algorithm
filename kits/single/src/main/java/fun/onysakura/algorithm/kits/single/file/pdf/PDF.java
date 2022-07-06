package fun.onysakura.algorithm.kits.single.file.pdf;

import com.benjaminwan.ocrlibrary.OcrResult;
import fun.onysakura.algorithm.kits.single.file.image.OCRUtils;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDResources;
import org.apache.pdfbox.pdmodel.graphics.PDXObject;
import org.apache.pdfbox.pdmodel.graphics.form.PDFormXObject;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

import javax.imageio.ImageIO;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PDF {

    public static void main(String[] args) throws Exception {
        String tempImagePath = "C:/Files/Temp/temp.png";
        try (PDDocument document = Loader.loadPDF(new File("C:\\Users\\gjp06\\Documents\\WeChat Files\\wxid_08la6dlb4zm521\\FileStorage\\File\\2022-01\\附件2-员工信息安全手册-2.20211217.pdf"))) {
            PDPage page = document.getPage(3);
            List<RenderedImage> images = getImagesFromResources(page.getResources());
            for (RenderedImage image : images) {
                ImageIO.write(image, "png", new File(tempImagePath));
                OcrResult result = OCRUtils.detect(tempImagePath);
                System.out.println(result.getStrRes());
            }
        }
    }

    private static List<RenderedImage> getImagesFromResources(PDResources resources) throws IOException {
        List<RenderedImage> images = new ArrayList<>();
        for (COSName xObjectName : resources.getXObjectNames()) {
            PDXObject xObject = resources.getXObject(xObjectName);
            if (xObject instanceof PDFormXObject) {
                images.addAll(getImagesFromResources(((PDFormXObject) xObject).getResources()));
            } else if (xObject instanceof PDImageXObject) {
                images.add(((PDImageXObject) xObject).getImage());
            }
        }
        return images;
    }

}
