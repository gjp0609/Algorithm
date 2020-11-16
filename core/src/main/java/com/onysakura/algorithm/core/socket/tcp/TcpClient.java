package com.onysakura.algorithm.core.socket.tcp;

import com.onysakura.algorithm.core.utils.Benchmark;

import java.net.Socket;

public class TcpClient {
    public static String host = "140.143.236.131";
    public static int port = 30001;

    public static void main(String[] args) throws Exception {
        Benchmark.init();
        for (int j = 0; j < 10; j++) {
            Benchmark.begin();
            for (int i = 0; i < 1000; i++) {
                Socket socket = new Socket(host, port);
                TcpUtils.send(socket, String.valueOf(System.currentTimeMillis()));
                socket.close();
            }
            Benchmark.end();
            Thread.sleep(1000);
        }
    }
}
