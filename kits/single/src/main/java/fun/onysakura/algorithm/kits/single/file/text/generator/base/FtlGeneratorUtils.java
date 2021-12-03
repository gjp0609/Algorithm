package fun.onysakura.algorithm.kits.single.file.text.generator.base;

import freemarker.template.Configuration;
import freemarker.template.Template;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;

public class FtlGeneratorUtils {

    private static final String TEMPLATE_PATH = "kits/single/src/main/resources/template";
    private static final String OUTPUT_PATH = "kits/single/src/main/resources/output";
    private static final Configuration configuration;

    static {
        configuration = new Configuration(Configuration.VERSION_2_3_0);
        File file = new File(TEMPLATE_PATH);
        try {
            configuration.setDirectoryForTemplateLoading(file);
        } catch (Exception ignored) {
        }
    }

    public static void generate(FtlTemplate ftlTemplate, String dir, String file, Object obj) throws Exception {
        Template template = configuration.getTemplate(ftlTemplate.name() + ".ftl");
        new File(OUTPUT_PATH + "/" + dir).mkdirs();
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(OUTPUT_PATH + "/" + dir + "/" + file)));
        template.process(obj, writer);
    }
}
