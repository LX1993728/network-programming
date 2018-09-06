package com.liuxun.bio2;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author liuxun
 * bio 在这里表示是Blocking IO阻塞的IO
 */
public class Server {
    final static int PORT = 8765;

    public static void main(String[] args) {
        ServerSocket server = null;
        try {
            server = new ServerSocket(PORT);
            System.out.println(" server start ...");
            Socket socket = null;
            HandlerExecutorPool executorPool = new HandlerExecutorPool(50, 100);
            while (true){
                socket = server.accept();
                executorPool.execute(new ServerHandler(socket));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (server != null) {
                try {
                    server.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            server = null;
        }
    }
}
