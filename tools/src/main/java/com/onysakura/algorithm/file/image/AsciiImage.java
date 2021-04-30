package com.onysakura.algorithm.file.image;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;

public class AsciiImage {

    private static final ColorDistanceCalc.Type COLOR_DISTANCE_CALC_TYPE = ColorDistanceCalc.Type.CIELab;
    private static final String GRAYSCALE_SYMBOL = "+-·";
    private static final ArrayList<Float> GRAYSCALE_LIST = new ArrayList<>();
    private static final Map<Color, String> SUPPORTED_COLORS = new HashMap<>();
    private static boolean SAVE_IMAGE = false;
    private static int MAX_HEIGHT = 0;
    private static int MAX_WIDTH = 0;

    static {
        SUPPORTED_COLORS.put(new Color(0, 0, 0), "30");
        SUPPORTED_COLORS.put(new Color(205, 0, 0), "31");
        SUPPORTED_COLORS.put(new Color(0, 205, 0), "32");
        SUPPORTED_COLORS.put(new Color(255, 142, 3), "33");
        SUPPORTED_COLORS.put(new Color(0, 0, 238), "34");
        SUPPORTED_COLORS.put(new Color(205, 0, 205), "35");
        SUPPORTED_COLORS.put(new Color(0, 204, 204), "36");
        SUPPORTED_COLORS.put(new Color(170, 170, 170), "37");
        SUPPORTED_COLORS.put(new Color(85, 85, 85), "90");
        SUPPORTED_COLORS.put(new Color(255, 0, 0), "91");
        SUPPORTED_COLORS.put(new Color(0, 255, 0), "92");
        SUPPORTED_COLORS.put(new Color(255, 168, 9), "93");
        SUPPORTED_COLORS.put(new Color(92, 92, 255), "94");
        SUPPORTED_COLORS.put(new Color(255, 0, 255), "95");
        SUPPORTED_COLORS.put(new Color(0, 255, 255), "96");
        SUPPORTED_COLORS.put(new Color(255, 255, 255), "97");
    }

    public static void main(String[] args) {
        printImage("/Files/Temp/code.jpg");
    }

    public static void printImage(String path, int maxHeight, int maxWidth, boolean saveImage) {
        MAX_HEIGHT = maxHeight;
        MAX_WIDTH = maxWidth;
        SAVE_IMAGE = saveImage;
        printImage(path);
    }

    public static void printImage(String path) {
        BufferedImage image = getImage(path);
        ConcurrentMap<Color, Double> colorDifferenceMap = new ConcurrentHashMap<>();
        ExecutorService executorService = Executors.newFixedThreadPool(
                Runtime.getRuntime().availableProcessors());
        for (Color color : SUPPORTED_COLORS.keySet()) {
            executorService.execute(() -> {
                double sum = 0d;
                for (int y = 0; y < image.getHeight(); y++) {
                    for (int x = 0; x < image.getWidth(); x++) {
                        Color pixelColor = new Color(image.getRGB(x, y));
                        sum += calcColorDistance(color, pixelColor, COLOR_DISTANCE_CALC_TYPE);
                    }
                }
                colorDifferenceMap.put(color, sum);
            });
        }
        executorService.shutdown();
        try {
            boolean b = executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.DAYS);
            if (!b) {
                throw new RuntimeException("awaitTermination fail");
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        BufferedImage scale = scale(image);
        List<Color> colorSolutions = colorDifferenceMap.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
        int i = scale.getHeight() * scale.getWidth();
        for (int y = 0; y < scale.getHeight(); y++) {
            for (int x = 0; x < scale.getWidth(); x++) {
                int p = (scale.getWidth() * y + x) * 100 / i;
                System.out.print("\r" + p + "%");
                Color color1 = new Color(scale.getRGB(x, y));
                Optional<Color> min = colorSolutions.stream().min(Comparator.comparingDouble(color2 -> calcColorDistance(color1, color2, COLOR_DISTANCE_CALC_TYPE)));
                if (min.isPresent()) {
                    Color closestColor = min.get();
                    scale.setRGB(x, y, closestColor.getRGB());
                }
            }
        }
        System.out.print("\r100%");
        System.out.println();
        if (SAVE_IMAGE) {
            try {
                String suffix = path.substring(path.lastIndexOf(".") + 1);
                String pathDir = path.substring(0, path.lastIndexOf("/"));
                ImageIO.write(scale, suffix, new File(pathDir + "/temp." + suffix));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        setPrint(scale);
        for (int y = 0; y < scale.getHeight(); y++) {
            for (int x = 0; x < scale.getWidth(); x++) {
                int rgb = scale.getRGB(x, y);
                System.out.print(getPrint(rgb));
            }
            System.out.println();
        }
    }

    public static void pr() {
        for (int i = 0; i < 120; i++) {
            System.out.print("\033[" + i + "m" + String.format("%4d", i) + "\033[0m");
            if (i % 10 == 0) {
                System.out.println();
            }
        }
    }

    public static void setPrint(BufferedImage image) {
        HashSet<Float> floats = new HashSet<>();
        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {
                int rgb = image.getRGB(x, y);
                float grayscale = convertRGBToGrayscale(rgb);
                floats.add(grayscale);
            }
        }
        ArrayList<Float> list = new ArrayList<>(floats);
        Collections.sort(list);
        int length = GRAYSCALE_SYMBOL.length();
        for (int i = 0; i < length; i++) {
            Float aFloat = list.get(list.size() * i / length);
            GRAYSCALE_LIST.add(aFloat);
        }
        System.out.println(GRAYSCALE_LIST);
    }

    public static String getPrint(int rgb) {
        Color color = getRGB(rgb);
        String s = SUPPORTED_COLORS.get(color);
        return "\033[" + s + "m" + " `·" + "\033[0m";
//        float grayscale = convertRGBToGrayscale(rgb);
//        String print = getPrint(grayscale);
//        return "\033[" + s + "m" + print + print + print + "\033[0m";
    }

    public static String getPrint(float grayscale) {
        for (int i = 0; i < GRAYSCALE_LIST.size(); i++) {
            if (grayscale <= GRAYSCALE_LIST.get(i)) {
                return String.valueOf(GRAYSCALE_SYMBOL.charAt(i));
            }
        }
        return "#";
    }

    private static Color getRGB(final int rgbColor) {
        int red = (rgbColor >> 16) & 0xFF;
        int green = (rgbColor >> 8) & 0xFF;
        int blue = rgbColor & 0xFF;
        return new Color(red, green, blue);
    }

    private static float convertRGBToGrayscale(final int rgbColor) {
        // extract components
        int red = (rgbColor >> 16) & 0xFF;
        int green = (rgbColor >> 8) & 0xFF;
        int blue = rgbColor & 0xFF;
        // convert to grayscale
        return 0.3f * red + 0.59f * green + 0.11f * blue;
    }

    public static BufferedImage getImage(String path) {
        BufferedImage image = null;
        try {
            image = ImageIO.read(new File(path));
        } catch (IOException e) {
            System.err.println("未能获取到图片");
            e.printStackTrace();
        }
        //   ◾ ◼ ▝ ▘▖▗ ▌▐ ▄▀ ▚ ▞ ▛ ▜ ▟ ▙ █
        return image;
    }

    public static BufferedImage scale(BufferedImage original) {
        if (MAX_HEIGHT == 0 && MAX_WIDTH == 0) {
            return original;
        }
        int newWidth = MAX_WIDTH;
        int newHeight = MAX_HEIGHT;
        if (MAX_WIDTH == 0) {
            newWidth = (int) (original.getWidth() * MAX_HEIGHT * 1D / original.getHeight());
        } else if (MAX_HEIGHT == 0) {
            newHeight = (int) (original.getHeight() * MAX_WIDTH * 1D / original.getWidth());
        } else {
            if (1D * original.getWidth() / original.getHeight() > 1D * MAX_WIDTH / MAX_HEIGHT) {
                newHeight = (int) (original.getHeight() * MAX_WIDTH * 1D / original.getWidth());
            } else {
                newWidth = (int) (original.getWidth() * MAX_HEIGHT * 1D / original.getHeight());
            }
        }
        BufferedImage resized = new BufferedImage(newWidth, newHeight, original.getType());
        Graphics2D g = resized.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g.drawImage(original, 0, 0, newWidth, newHeight, 0, 0, original.getWidth(),
                original.getHeight(), null);
        g.dispose();
        return resized;
    }

    public static double calcColorDistance(Color c1, Color c2, ColorDistanceCalc.Type type) {
        switch (type) {
            case CIELab:
                return CIELab.from(c1).difference(CIELab.from(c2));
            case RGB:
                return RGB.from(c1).difference(RGB.from(c2));
        }
        return 0;
    }

    interface ColorDistanceCalc {
        enum Type {
            RGB(),
            CIELab();
        }
    }

    static class RGB implements ColorDistanceCalc {
        private final double r, g, b;

        public RGB(double r, double g, double b) {
            this.r = r;
            this.g = g;
            this.b = b;
        }

        public double difference(RGB rgb) {
            return Math.sqrt(Math.pow(rgb.r - r, 2) + Math.pow(rgb.g - g, 2) + Math.pow(rgb.b - b, 2));
        }

        public static RGB from(Color color) {
            return new RGB(color.getRed(), color.getGreen(), color.getBlue());
        }
    }

    static class CIELab implements ColorDistanceCalc {

        private final double L, a, b;

        public CIELab(double L, double a, double b) {
            this.L = L;
            this.a = a;
            this.b = b;
        }

        public double difference(CIELab cieLab) {
            return Math.sqrt(Math.pow(cieLab.L - L, 2) + Math.pow(cieLab.a - a, 2) +
                    Math.pow(cieLab.b - b, 2));
        }

        public static CIELab from(Color color) {
            int sR = color.getRed();
            int sG = color.getGreen();
            int sB = color.getBlue();

            // Convert Standard-RGB to XYZ (http://www.easyrgb.com/en/math.php)
            double var_R = (sR / 255d);
            double var_G = (sG / 255d);
            double var_B = (sB / 255d);

            if (var_R > 0.04045) var_R = Math.pow((var_R + 0.055) / 1.055, 2.4);
            else var_R = var_R / 12.92;
            if (var_G > 0.04045) var_G = Math.pow((var_G + 0.055) / 1.055, 2.4);
            else var_G = var_G / 12.92;
            if (var_B > 0.04045) var_B = Math.pow((var_B + 0.055) / 1.055, 2.4);
            else var_B = var_B / 12.92;

            var_R = var_R * 100;
            var_G = var_G * 100;
            var_B = var_B * 100;

            double X = var_R * 0.4124 + var_G * 0.3576 + var_B * 0.1805;
            double Y = var_R * 0.2126 + var_G * 0.7152 + var_B * 0.0722;
            double Z = var_R * 0.0193 + var_G * 0.1192 + var_B * 0.9505;

            // Convert XYZ to CIELAB (http://www.easyrgb.com/en/math.php
            double var_X = X / 96.422;
            double var_Y = Y / 100.000;
            double var_Z = Z / 82.521;

            if (var_X > 0.008856) var_X = Math.pow(var_X, 1D / 3D);
            else var_X = (7.787 * var_X) + (16D / 116);
            if (var_Y > 0.008856) var_Y = Math.pow(var_Y, 1D / 3D);
            else var_Y = (7.787 * var_Y) + (16D / 116);
            if (var_Z > 0.008856) var_Z = Math.pow(var_Z, 1D / 3D);
            else var_Z = (7.787 * var_Z) + (16D / 116);

            double L = (116 * var_Y) - 16;
            double a = 500 * (var_X - var_Y);
            double b = 200 * (var_Y - var_Z);

            return new CIELab(L, a, b);
        }
    }
}
