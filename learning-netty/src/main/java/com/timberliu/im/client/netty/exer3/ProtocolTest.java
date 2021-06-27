package com.timberliu.im.client.netty.exer3;

import com.timberliu.im.client.netty.exer3.protocol.command.AbstractPacket;
import com.timberliu.im.client.netty.exer3.protocol.command.LoginRequestPacket;
import com.timberliu.im.client.netty.exer3.protocol.command.PacketCodec;
import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.time.LocalDateTime;
import java.util.Random;

/**
 * Created by liujie on 2021/6/22
 */

public class ProtocolTest {

    public static void main(String[] args) throws Exception {
        new Thread(() -> {
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            NioEventLoopGroup bossGroup = new NioEventLoopGroup();
            NioEventLoopGroup workerGroup = new NioEventLoopGroup();
            serverBootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<NioSocketChannel>() {
                        @Override
                        protected void initChannel(NioSocketChannel channel) throws Exception {
                            channel.pipeline().addLast(new SimpleChannelInboundHandler<Object>() {
                                @Override
                                protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
                                    AbstractPacket loginRequestPacket = new PacketCodec().decode((ByteBuf) msg);
                                    System.out.println("[" + Thread.currentThread().getName() + "] " + LocalDateTime.now() + ": 接收" + loginRequestPacket + "\n");
                                }
                            });
                        }
                    })
                    .bind(12345);
        }).start();

        NioEventLoopGroup clientEventLoopGroup = new NioEventLoopGroup();
        Bootstrap clientBootstrap = new Bootstrap();
        Channel channel = clientBootstrap.group(clientEventLoopGroup)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<Channel>() {
                    @Override
                    protected void initChannel(Channel channel) throws Exception {
                    }
                })
                .connect("127.0.0.1", 12345).sync().channel();
        while (true) {
            LoginRequestPacket loginRequestPacket = new LoginRequestPacket();
            loginRequestPacket.setUserId(new Random().nextInt(5));
            loginRequestPacket.setUsername(String.valueOf(new Random().nextInt(5)));
            loginRequestPacket.setPassword(String.valueOf(new Random().nextInt(5)));
            ByteBuf byteBuf = new PacketCodec().encode(loginRequestPacket);
            channel.writeAndFlush(byteBuf);
            System.out.println("[" + Thread.currentThread().getName() + "] " + LocalDateTime.now() + ": 发送" + loginRequestPacket);
            Thread.sleep(2000);
        }

    }
}
