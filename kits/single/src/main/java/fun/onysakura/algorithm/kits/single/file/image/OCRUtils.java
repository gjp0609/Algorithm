/*
 * from https://github.com/DayBreak-u/chineseocr_lite
 */
package fun.onysakura.algorithm.kits.single.file.image;

import com.benjaminwan.ocrlibrary.OcrEngine;
import com.benjaminwan.ocrlibrary.OcrResult;
import fun.onysakura.algorithm.kits.single.Constants;
import lombok.extern.slf4j.Slf4j;

import java.io.File;

@Slf4j
public class OCRUtils {
    private final static String MODELS_PATH = Constants.RESOURCES_PATH + "/ocr/models/";
    private final static String DLL_PATH = new File(Constants.RESOURCES_PATH).getAbsolutePath() + "/ocr/lib/OcrLiteNcnnGpu.dll";
    private final static int MAX_SIDE_LEN = 0; //按图像长边进行总体缩放，放大增加识别耗时但精度更高，缩小减小耗时但精度降低，maxSideLen=0 代表不缩放
    private final static OcrEngine ENGINE = new OcrEngine();

    static {
        System.load(DLL_PATH);
        init();
    }

    public static void main(String[] args) {
        OcrResult detect = detect("C:/Files/Temp/Snipaste_2021-12-16_16-28-10.jpg");
        log.info("result: \n{}", detect.getStrRes());
    }

    public static OcrResult detect(String imgPath) {
        return ENGINE.detect(imgPath, MAX_SIDE_LEN);
    }

    private static void init() {
        log.debug("init ocr engine");
        ENGINE.setNumThread(4);
        ENGINE.initLogger(false, false, false);
        ENGINE.setGpuIndex(0); // GPU0 一般为默认 GPU，参数选项：使用 CPU(-1) / 使用GPU0(0) / 使用GPU1(1) / ...
        boolean initModelsRet = ENGINE.initModels(MODELS_PATH, "dbnet_op", "angle_op", "crnn_lite_op", "keys.txt");
        if (!initModelsRet) {
            log.warn("Error in models initialization, please check the models/keys path!");
            return;
        }
        ENGINE.setPadding(50); // 图像外接白框，用于提升识别率，文字框没有正确框住所有文字时，增加此值。
        ENGINE.setBoxScoreThresh(0.6f); // 文字框置信度门限，文字框没有正确框住所有文字时，减小此值
        ENGINE.setBoxThresh(0.3f); // 请自行试验
        ENGINE.setUnClipRatio(2f); // 单个文字框大小倍率，越大时单个文字框越大
        ENGINE.setDoAngle(false); // 启用/禁用文字方向检测，只有图片倒置的情况下(旋转90~270度的图片)，才需要启用文字方向检测
        ENGINE.setMostAngle(false); // 启用/禁用角度投票(整张图片以最大可能文字方向来识别)，当禁用文字方向检测时，此项也不起作用
        String version = ENGINE.getVersion();
        log.debug("engine init success, version: {}", version);
    }
}
