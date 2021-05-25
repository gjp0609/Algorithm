package com.onysakura.algorithm.file.text;

import com.onysakura.algorithm.utilities.basic.Benchmark;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 
 */
public class CountLine {

    private static final String[] IGNORES_FILES_TYPE = new String[]{
            ".class",
            ".http",
            ".ico",
            ".iml",
            ".jpg",
            ".png",
            ".rest",
            ".ttf",
            ".woff",
            ".gitignore",
    };
    private static final String[] IGNORES_DIRS = new String[]{
            ".idea",
            ".svn",
            "node_modules",
            "target",
            "dist",
    };

    private static final ConcurrentHashMap<String, AtomicLong> FILE_COUNT = new ConcurrentHashMap<>();
    private static final AtomicLong LINES_COUNT = new AtomicLong();

    public static void main(String[] args) throws Exception {
        Benchmark.init();
        File file = new File("C:/Files/");
        if (file.exists() && file.isDirectory()) {
            Benchmark.begin();
            readDir(file);
            Benchmark.end();
        }
        System.out.println(LINES_COUNT);
        for (Map.Entry<String, AtomicLong> entry : FILE_COUNT.entrySet()) {
            System.out.printf("%20s: %10d\n", entry.getKey(), entry.getValue().get());
        }
    }

    public static void readDir(File dir) throws Exception {
        for (String ignore : IGNORES_DIRS) {
            if (dir.getAbsolutePath().endsWith(ignore)) {
                return;
            }
        }
        File[] files = dir.listFiles();
        if (files != null) {
            for (File f : files) {
                if (f.isDirectory()) {
                    readDir(f);
                } else {
                    try {
                        readFile(f);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    public static void readFile(File file) throws Exception {
        String fileName = file.getName();
        for (String ignoresFileType : IGNORES_FILES_TYPE) {
            if (fileName.endsWith(ignoresFileType)) {
                return;
            }
        }
        String fileType = fileName.substring(fileName.lastIndexOf("."));
        count(fileType);
        BufferedReader reader = new BufferedReader(new FileReader(file));
        long count = reader.lines().count();
        LINES_COUNT.addAndGet(count);
    }

    public static void count(String fileType) {
        if (FILE_COUNT.containsKey(fileType)) {
            AtomicLong atomicLong = FILE_COUNT.get(fileType);
            atomicLong.incrementAndGet();
        } else {
            FILE_COUNT.put(fileType, new AtomicLong(1L));
        }
    }
}
