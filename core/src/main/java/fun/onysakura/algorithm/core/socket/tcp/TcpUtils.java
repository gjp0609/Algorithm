package fun.onysakura.algorithm.core.socket.tcp;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.function.Function;

public class TcpUtils {

    public static void send(Socket socket, String content) throws IOException {
        OutputStream os = socket.getOutputStream();
        os.write(content.getBytes());
    }

    public static String sendAndReceive(Socket socket, String content) throws IOException {
        // send
        send(socket, content);
        // receive
        InputStream is = socket.getInputStream();
        byte[] arr = new byte[1024];
        int len = is.read(arr);
        return new String(arr, 0, len);
    }

    public static void receive(ServerSocket server, Function<String, String> function) throws IOException {
        Socket socket = server.accept();
        InputStream is = socket.getInputStream();
        byte[] arr = new byte[1024];
        int len = is.read(arr);
        send(socket, function.apply(new String(arr, 0, len)));
        socket.close();
    }

}
