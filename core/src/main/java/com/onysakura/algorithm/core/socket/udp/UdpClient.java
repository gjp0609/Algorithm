package com.onysakura.algorithm.core.socket.udp;

import com.onysakura.algorithm.utilities.basic.Benchmark;

import java.net.DatagramSocket;

public class UdpClient {

    public static String host = "140.143.236.131";
    public static int port = 30002;

    public static void main(String[] args) throws Exception {
        Benchmark.init();
        for (int j = 0; j < 10; j++) {
            Benchmark.begin();
            for (int i = 0; i < 1000; i++) {
                DatagramSocket clientSocket = new DatagramSocket();
                UdpUtils.send(clientSocket, host, port, String.valueOf(System.currentTimeMillis()));
                clientSocket.close();
            }
            Benchmark.end();
            Thread.sleep(1000);
        }
    }
}
