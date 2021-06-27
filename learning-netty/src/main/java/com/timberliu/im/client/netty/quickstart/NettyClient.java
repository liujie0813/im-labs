package com.timberliu.im.client.netty.quickstart;

import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.util.AttributeKey;
import org.omg.CORBA.TIMEOUT;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

/**
 * Created by liujie on 2021/6/21
 */

public class NettyClient {

    private static final int MAX_RETRY = 3;

    public static void main(String[] args) throws InterruptedException {
        Bootstrap bootstrap = new Bootstrap();
        NioEventLoopGroup group = new NioEventLoopGroup();

        bootstrap
                // 指定线程模型
                .group(group)
                // 指定 IO 模型为 NIO
                .channel(NioSocketChannel.class)
                // IO 处理逻辑
                .handler(new ChannelInitializer<Channel>() {
                    @Override
                    protected void initChannel(Channel channel) throws Exception {
                        channel.pipeline().addLast(new StringEncoder());
                    }
                })
                .attr(AttributeKey.newInstance("clientName"), "nettyClient")
                // 设置一些 TCP 底层属性
                    // 连接超时时间
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000)
                    .option(ChannelOption.SO_KEEPALIVE, true)
                    .option(ChannelOption.TCP_NODELAY, true);

        // sync() 保证建立连接完成后再 write
        Channel channel = connect(bootstrap, "127.0.0.1", 445, MAX_RETRY);

        while (true) {
            channel.writeAndFlush(LocalDateTime.now() + ": hello world !");
            Thread.sleep(2000);
        }
    }

    private static Channel connect(Bootstrap bootstrap, String host, int port, int retry) {
        return bootstrap.connect(host, port)
                .addListener(future -> {
                    if (future.isSuccess()) {
                        System.out.println("连接成功!");
                    } else if (retry == 0) {
                        System.err.println("重试次数用完，放弃连接!");
                    } else {
                        int order = (MAX_RETRY - retry) + 1;
                        int delay = 1 << order;
                        System.out.println("连接失败，第 " + order + " 次重连！");
                        // config 是对 Bootstrap 配置参数的抽象，group 返回的就是一开始配置的 NioEventLoopGroup
                        bootstrap.config().group().schedule(
                                () -> connect(bootstrap, host, port, retry - 1),
                                delay,
                                TimeUnit.SECONDS);
                    }
                }).channel();
    }

}
