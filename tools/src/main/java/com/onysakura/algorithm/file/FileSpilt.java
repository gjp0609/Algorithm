package com.onysakura.algorithm.file;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class FileSpilt {

    private static final String SQL_PATH = "/Files/Workspaces/Mine/tzkbapp.sql";

    public static void main(String[] args) throws Exception {
        splitFile(SQL_PATH, 5);
    }

    /**
     * 把文件分割为多个等大的文件
     */
    public static void splitFile(String filePath, int fileCount) throws IOException {
        System.out.println("start");
        FileInputStream fis = new FileInputStream(filePath);
        FileChannel channel = fis.getChannel();
        long fileSize = channel.size();
        long average = fileSize / fileCount;// 平均值
        int bufferSize = 200; // 缓存块大小，自行调整
        ByteBuffer byteBuffer = ByteBuffer.allocate(bufferSize);// 申请一个缓存区
        long startPosition = 0; //子文件开始位置
        long endPosition = average < bufferSize ? 0 : average - bufferSize;//子文件结束位置
        for (int i = 0; i < fileCount; i++) {
            System.out.println("i: " + i);
            if (i + 1 != fileCount) {
                int read = channel.read(byteBuffer, endPosition); // 读取数据
                readW:
                while (read != -1) {
                    byteBuffer.flip(); //切换读模式
                    byte[] array = byteBuffer.array();
                    for (int j = 0; j < array.length; j++) {
                        int b = array[j];
                        if (b == 10 || b == 13) { //判断\r\n
                            endPosition += j;
                            break readW;
                        }
                    }
                    endPosition += bufferSize;
                    byteBuffer.clear(); // 重置缓存块指针
                    read = channel.read(byteBuffer, endPosition);
                }
            } else {
                endPosition = fileSize; // 最后一个文件直接指向文件末尾
            }
            FileOutputStream fos = new FileOutputStream(filePath.substring(0, filePath.lastIndexOf(".")) + (i + 1) + filePath.substring(filePath.lastIndexOf(".")));
            FileChannel outputChannel = fos.getChannel();
            channel.transferTo(startPosition, endPosition - startPosition, outputChannel); // 通道传输文件数据
            outputChannel.close();
            fos.close();
            startPosition = endPosition + 1;
            endPosition += average;
        }
        channel.close();
        fis.close();
    }
}
