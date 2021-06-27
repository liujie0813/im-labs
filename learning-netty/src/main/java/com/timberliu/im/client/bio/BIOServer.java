package com.timberliu.im.client.bio;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 传统 IO 方式
 *
 *   问题：
 *    1. 线程资源受限：同一时刻有大量线程处于阻塞状态，浪费资源
 *    2. 线程频繁切换：系统性能急剧下降
 *    3. 数据读写采用字节流为单位
 *
 * Created by liujie on 2021/6/20
 */

public class BIOServer {

    public static void main(String[] args) throws Exception {
        ExecutorService threadPool = Executors.newCachedThreadPool();
        // 创建一个 ServerSocket，监听 9999 端口
        ServerSocket serverSocket = new ServerSocket(9999);
        System.out.println("[" + Thread.currentThread().getName() + "] server started");

        while (true) {
            System.out.println("[" + Thread.currentThread().getName() + "] waiting for connection");
            final Socket socket = serverSocket.accept();
            // 每次有新连接时，使用一个新线程处理
            threadPool.execute(() -> handle(socket));
        }
    }

    private static void handle(Socket socket) {
        System.out.println("[" + Thread.currentThread().getName() + "] client is connected");
        byte[] cache = new byte[1024];
        try (InputStream inputStream = socket.getInputStream()) {
            while (true) {
                System.out.println("[" + Thread.currentThread().getName() + "] waiting for input");
                int readLen = inputStream.read(cache);
                if (readLen == -1) {
                    break;
                }
                System.out.println("[" + Thread.currentThread().getName() + "] " + new String(cache, 0, readLen));
            }
            System.out.println("[" + Thread.currentThread().getName() + "] connection broken");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
