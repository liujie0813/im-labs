package com.timberliu.im.client.netty.channel.handler;

import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.util.AttributeKey;

import java.util.concurrent.TimeUnit;

import static com.timberliu.im.client.netty.channel.handler.Attributes.ATTRIBUTE_KEY;

/**
 * Created by liujie on 2021/6/22
 */

public class CountUserHandlerTest {

    public static void main(String[] args) throws Exception {
        NioEventLoopGroup bossGroup = new NioEventLoopGroup();
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        serverBootstrap.group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel ch) throws Exception {
                        ch.pipeline().addLast(new CountUserHandler());
                        ch.pipeline().addLast(new StringDecoder());
                        ch.pipeline().addLast(new ChannelInboundHandlerAdapter() {
                            @Override
                            public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                                System.out.println(msg);
                            }
                        });
                        serverBootstrap.config().group().scheduleAtFixedRate(() -> {
                            System.out.println(ch.attr(ATTRIBUTE_KEY).get());
                        }, 0, 2, TimeUnit.SECONDS);
                    }
                })
                .bind(8888);
        Thread.sleep(2000);

        for (int i = 0; i < 5; i++) {
            new Thread(() -> {
                NioEventLoopGroup clientGroup = new NioEventLoopGroup();
                Bootstrap bootstrap = new Bootstrap();
                Channel channel = null;
                try {
                    channel = bootstrap.group(clientGroup)
                            .channel(NioSocketChannel.class)
                            .handler(new ChannelInitializer<Channel>() {
                                @Override
                                protected void initChannel(Channel ch) throws Exception {
                                    ch.pipeline().addLast(new StringEncoder());
                                }
                            })
                            .connect("127.0.0.1", 8888).sync().channel();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                while (true) {
                    channel.writeAndFlush("hello");
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
    }
}

class CountUserHandler extends ChannelInboundHandlerAdapter {

    private static Integer count = 0;

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        count++;
        ctx.channel().attr(ATTRIBUTE_KEY).set(count);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        count--;
        ctx.channel().attr(ATTRIBUTE_KEY).set(count);
    }
}
