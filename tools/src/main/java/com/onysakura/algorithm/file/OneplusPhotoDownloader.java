package com.onysakura.algorithm.file;

import com.drew.imaging.ImageMetadataReader;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.Tag;
import com.onysakura.algorithm.utilities.basic.idGenerator.IdUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * 下载一加云相册照片
 * url获取参考：https://kouss.com/2018-10-12.html
 */
@Slf4j
public class OneplusPhotoDownloader {

    private static final String URL_FILE = "C:\\Users\\gjp06\\Downloads\\dd.txt";
    private static final String DOWNLOAD_PATH = "C:\\Users\\gjp06\\Downloads\\20210714dd";

    public static void main(String[] args) throws Exception {
        File downloadPath = new File(DOWNLOAD_PATH);
        downloadPath.mkdirs();
        ExecutorService pool = Executors.newFixedThreadPool(5);
        File file = new File(URL_FILE);
        BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
        bufferedReader.lines().forEach(url -> {
            pool.submit(() -> {
                long id = download(url);
                File image = new File(DOWNLOAD_PATH + "/" + id + ".png");
                String imageName = DOWNLOAD_PATH + "/" + getImageName(image);
                System.out.println(imageName);
                image.renameTo(new File(imageName));
            });
        });
        pool.shutdown();
        pool.awaitTermination(Integer.MAX_VALUE, TimeUnit.SECONDS);
    }

    public static long download(String url) {
        long id = IdUtils.snowflakeId();
        try {
            FileUtils.copyURLToFile(new URL(url), new File(DOWNLOAD_PATH + "/" + id + ".png"));
        } catch (Exception e) {
            log.warn("下载失败：{}", url);
        }
        return id;
    }

    public static String getImageName(File file) {
        Metadata metadata;
        try {
            metadata = ImageMetadataReader.readMetadata(file);
        } catch (Exception e) {
            return file.getName();
        }
        String name = String.valueOf(IdUtils.snowflakeId());
        String type = "png";
        Iterable<Directory> directories = metadata.getDirectories();
        for (Directory directory : directories) {
//            System.out.println(directory.getName());
            for (Tag tag : directory.getTags()) {
//                System.out.println(tag);
                switch (tag.getTagName()) {
                    case "Expected File Name Extension":
                        type = tag.getDescription();
                        break;
                    case "Date/Time Original":
                        LocalDateTime date = LocalDateTime.parse(tag.getDescription(), DateTimeFormatter.ofPattern("yyyy:MM:dd HH:mm:ss"));
                        name = date.format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
                        break;
                }
            }
        }
        return name + "." + type;
    }

}