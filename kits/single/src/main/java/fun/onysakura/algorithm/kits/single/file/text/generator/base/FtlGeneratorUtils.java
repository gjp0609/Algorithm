package fun.onysakura.algorithm.kits.single.file.text.generator.base;

import freemarker.template.Configuration;
import freemarker.template.Template;
import fun.onysakura.algorithm.kits.single.Constants;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;

public class FtlGeneratorUtils {

    private static final Configuration configuration;

    static {
        configuration = new Configuration(Configuration.VERSION_2_3_0);
        File file = new File(Constants.RESOURCES_PATH + "/template");
        try {
            configuration.setDirectoryForTemplateLoading(file);
        } catch (Exception ignored) {
        }
    }

    public static void generate(FtlTemplate ftlTemplate, String dir, String file, Object obj) throws Exception {
        Template template = configuration.getTemplate(ftlTemplate.name() + ".ftl");
        new File(Constants.OUTPUT_PATH + "/" + dir).mkdirs();
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(Constants.OUTPUT_PATH + "/" + dir + "/" + file)));
        template.process(obj, writer);
    }
}
