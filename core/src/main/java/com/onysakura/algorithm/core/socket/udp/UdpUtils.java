package com.onysakura.algorithm.core.socket.udp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.function.Function;

public class UdpUtils {

    public static void send(DatagramSocket socket, SocketAddress address, String content) throws IOException {
        DatagramPacket sendPacket = new DatagramPacket(content.getBytes(), content.getBytes().length, address);
        socket.send(sendPacket);
    }

    public static void send(DatagramSocket socket, String ip, int port, String content) throws IOException {
        InetSocketAddress address = new InetSocketAddress(ip, port);
        DatagramPacket sendPacket = new DatagramPacket(content.getBytes(), content.getBytes().length, address);
        socket.send(sendPacket);
    }

    public static String sendAndReceive(DatagramSocket socket, SocketAddress address, String content) throws IOException {
        DatagramPacket sendPacket = new DatagramPacket(content.getBytes(), content.getBytes().length, address);
        socket.send(sendPacket);
        byte[] receiveData = new byte[1024];
        DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length, address);
        socket.receive(receivePacket);
        return getResult(receivePacket);
    }

    public static String sendAndReceive(DatagramSocket socket, String ip, int port, String content) throws IOException {
        InetSocketAddress address = new InetSocketAddress(ip, port);
        return sendAndReceive(socket, address, content);
    }

    public static void receive(DatagramSocket socket, Function<String, String> function) throws IOException {
        byte[] receiveData = new byte[1024];
        DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
        socket.receive(receivePacket);
        send(socket, receivePacket.getSocketAddress(), function.apply(getResult(receivePacket)));
    }

    public static String getResult(DatagramPacket packet) {
        return new String(packet.getData(), packet.getOffset(), packet.getLength());
    }
}
