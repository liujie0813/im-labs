package com.timberliu.im.client.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.Set;

/**
 * NIO
 *
 *   1. 在 NIO 模型中，不是每来一个连接就创建一个线程
 *      而是由一个线程控制，把连接注册到 selector 上，通过 selector 批量检测是否有数据可读的连接，进而读取数据
 *
 *   2. 实际开发，开多个线程，每个线程管理一批连接
 *
 *   问题：
 *   1. NIO 入门不友好
 *   2. 对于 NIO 编程来说，一个合适的线程模型能发挥它的优势，但 JDK 没有实现
 *   3. JDK 的 NIO 底层由 epoll 实现，该实现会出现 空轮询 bug，导致 CPU 飙升 100%
 *
 * Created by liujie on 2021/6/21
 */

public class NIOServer {

    public static void main(String[] args) throws IOException {
        Selector serverSelector = Selector.open();
        Selector clientSelector = Selector.open();

        new Thread(() -> {
            // 线程1：接收连接
            try {
                ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();

                serverSocketChannel.bind(new InetSocketAddress(8888));
                serverSocketChannel.configureBlocking(false);
                serverSocketChannel.register(serverSelector, SelectionKey.OP_ACCEPT);

                while (true) {
                    // 超时时间为 10ms
                    if (serverSelector.select(10) > 0) {
                        Set<SelectionKey> selectionKeys = serverSelector.selectedKeys();
                        Iterator<SelectionKey> keyIterator = selectionKeys.iterator();
                        while (keyIterator.hasNext()) {
                            SelectionKey selectionKey = keyIterator.next();
                            if (selectionKey.isAcceptable()) {
                                try {
                                    SocketChannel socketChannel = ((ServerSocketChannel) selectionKey.channel()).accept();
                                    socketChannel.configureBlocking(false);
                                    socketChannel.register(clientSelector, SelectionKey.OP_READ);
                                    System.out.println("[" + Thread.currentThread().getName() + "] client is connected");
                                } finally {
                                    // 判断过的 SelectionKey 要移除
                                    keyIterator.remove();
                                }
                            }
                        }
                    }
                }

            } catch (IOException e) {
                // ignore
            }
        }).start();

        new Thread(() -> {
            // 线程2：读写数据
            try {
                while (true) {
                    if (clientSelector.select(10) > 0) {
                        Set<SelectionKey> selectionKeys = clientSelector.selectedKeys();
                        // 有多条连接有数据可读，进行遍历读取
                        Iterator<SelectionKey> keyIterator = selectionKeys.iterator();
                        while (keyIterator.hasNext()) {
                            SelectionKey selectionKey = keyIterator.next();
                            if (selectionKey.isReadable()) {
                                try {
                                    SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
                                    ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
                                    socketChannel.read(byteBuffer);
                                    byteBuffer.flip();
                                    System.out.println(Charset.defaultCharset().newDecoder().decode(byteBuffer).toString());
                                } finally {
                                    keyIterator.remove();
                                    selectionKey.interestOps(SelectionKey.OP_READ);
                                }
                            }
                        }
                    }
                }
            } catch (IOException e) {

            }
        }).start();
    }
}
