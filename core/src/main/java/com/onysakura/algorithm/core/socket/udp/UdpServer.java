package com.onysakura.algorithm.core.socket.udp;

import java.net.DatagramSocket;

public class UdpServer {

    public static int port = 30002;

    public static void main(String[] args) throws Exception {
        System.out.println("start");
        DatagramSocket serverSocket = new DatagramSocket(port);

        while (true) {
            UdpUtils.receive(serverSocket, s -> {
                long millis = System.currentTimeMillis();
                System.out.println(millis + " - " + s + " = " + (millis - Long.parseLong(s)));
                return String.valueOf(System.currentTimeMillis());
            });
        }
    }
}
