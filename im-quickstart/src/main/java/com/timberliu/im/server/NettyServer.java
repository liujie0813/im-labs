package com.timberliu.im.server;

import com.timberliu.im.codec.PacketCodecHandler;
import com.timberliu.im.codec.PacketDecoder;
import com.timberliu.im.codec.PacketEncoder;
import com.timberliu.im.codec.Spliter;
import com.timberliu.im.protocol.PacketCodec;
import com.timberliu.im.server.handler.*;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.AttributeKey;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import sun.misc.Unsafe;

/**
 * Created by liujie on 2021/6/22
 */

public class NettyServer {

    private static final int PORT = 8888;

    public static void main(String[] args) {
        NioEventLoopGroup bossGroup = new NioEventLoopGroup();
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();

        ServerBootstrap serverBootstrap = new ServerBootstrap();
        serverBootstrap.group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                // 每次有新连接到来时，都会调用 ChannelInitializer 的 initChannel 方法，每次都会 new 新的 handler
                .childHandler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel nioSocketChannel) throws Exception {
                        nioSocketChannel.pipeline().addLast(new IMIdleStateHandler());
                        nioSocketChannel.pipeline().addLast(new Spliter());
                        // 合并编解码器
                        nioSocketChannel.pipeline().addLast(PacketCodecHandler.INSTANCE);
                        nioSocketChannel.pipeline().addLast(LoginRequestHandler.INSTANCE);
                        nioSocketChannel.pipeline().addLast(HeartBeatRequestHandler.INSTANCE);
                        // 用户认证 handler
                        nioSocketChannel.pipeline().addLast(new AuthHandler());
                        nioSocketChannel.pipeline().addLast(IMHandler.INSTANCE);
                    }
                })
                .handler(new ChannelInitializer<NioServerSocketChannel>() {
                    @Override
                    protected void initChannel(NioServerSocketChannel ch) throws Exception {
                    System.out.println("NettyServer initialized on port: " + PORT);
                    }
                })
                .attr(AttributeKey.newInstance("serverName"), "nettyServer")
                .childAttr(AttributeKey.newInstance("clientKey"), "clientValue")
                .childOption(ChannelOption.SO_KEEPALIVE, true)
                .childOption(ChannelOption.TCP_NODELAY, true)
                .option(ChannelOption.SO_BACKLOG, 1024);

        bind(serverBootstrap, PORT);

    }

    private static void bind(final ServerBootstrap serverBootstrap, final int port) {
        serverBootstrap.bind(port)
                .addListener(new GenericFutureListener<Future<? super Void>>() {
                    @Override
                    public void operationComplete(Future<? super Void> future) throws Exception {
                        if (future.isSuccess()) {
                            System.out.println("NettyServer started on port: " + PORT);
                        } else {
                            System.out.println("NettyServer start failed on port: " + PORT + ", retrying !");
                            bind(serverBootstrap, port);
                        }
                    }
                });
    }

}
