package com.timberliu.im.client.netty.quickstart;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.util.AttributeKey;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;

/**
 * Created by liujie on 2021/6/21
 */

public class NettyServer {

    public static void main(String[] args) {
        // 可以看做是传统 IO 编程模型的线程组
        // 负责接收连接
        NioEventLoopGroup bossGroup = new NioEventLoopGroup();
        // 负责读写等
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();

        // 引导类，服务端启动工作
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        serverBootstrap
                // 1. 配置线程组，确定了"线程模型"
                .group(bossGroup, workerGroup)
                // 2. 指定 "IO模型" 为 NIO（BIO 为 OioServerSocketChannel）
                .channel(NioServerSocketChannel.class)
                // 3. 定义每条连接的数据读写 "业务处理逻辑"
                // 泛型 NioSocketChannel 是 Netty 对 NIO 类型连接的抽象
                .childHandler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel nioSocketChannel) throws Exception {
                        nioSocketChannel.pipeline().addLast(new StringDecoder());
                        nioSocketChannel.pipeline().addLast(new SimpleChannelInboundHandler<String>() {
                            @Override
                            protected void channelRead0(ChannelHandlerContext channelHandlerContext, String msg) throws Exception {
                                System.out.println(msg);
                            }
                        });
                    }
                })
                // 指定服务端启动过程中的一些逻辑
                .handler(new ChannelInitializer<NioServerSocketChannel>() {
                    @Override
                    protected void initChannel(NioServerSocketChannel ch) throws Exception {
                        System.out.println("服务端启动了。。。");
                    }
                })
                // 给服务端的 channel（NioServerSocketChannel）指定一些属性
                .attr(AttributeKey.newInstance("serverName"), "nettyServer")
                // 给每条连接指定一些属性
                .childAttr(AttributeKey.newInstance("clientKey"), "clientValue")
                // 给每条 TCP 连接设置一些底层的属性
                    // 是否开启TCP底层心跳机制，true 表示开启
                .childOption(ChannelOption.SO_KEEPALIVE, true)
                    // 是否开启 Nagle 算法，true 表示关闭
                    // 如果要求高实时性，有数据发送时就马上发送，就关闭；如果需要减少发送次数减少网络交互，就开启
                    .childOption(ChannelOption.TCP_NODELAY, true)
                // 给服务端 channel 设置一些底层属性
                    // 系统用于临时存放已完成三次握手的请求的队列的最大长度
                .option(ChannelOption.SO_BACKLOG, 1024);

        // 自动递增端口绑定
        bind(serverBootstrap, 445);
    }

    private static void bind(final ServerBootstrap serverBootstrap, final int port) {
        serverBootstrap.bind(port)
                .addListener(new GenericFutureListener<Future<? super Void>>() {
                    @Override
                    public void operationComplete(Future<? super Void> future) throws Exception {
                        if (future.isSuccess()) {
                            System.out.println("端口 [" + port + "] 绑定成功");
                        } else {
                            System.out.println("端口 [" + port + "] 绑定失败，重试");
                            bind(serverBootstrap, port);
                        }
                    }
                });
    }
}
