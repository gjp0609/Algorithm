package com.onysakura.algorithm.core.socket.tcp;

import java.net.ServerSocket;

public class TcpServer {

    public static int port = 30001;

    public static void main(String[] args) throws Exception {
        System.out.println("start");
        ServerSocket server = new ServerSocket(port);

        while (true) {
            TcpUtils.receive(server, s -> {
                long millis = System.currentTimeMillis();
                System.out.println(millis + " - " + s + " = " + (millis - Long.parseLong(s)));
                return String.valueOf(System.currentTimeMillis());
            });
        }
    }
}
