package com.timberliu.im.client.netty.exer1;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.nio.charset.Charset;
import java.time.LocalDateTime;

/**
 * Created by liujie on 2021/6/21
 */

public class NettyServer {

    public static void main(String[] args) {
        NioEventLoopGroup bossGroup = new NioEventLoopGroup();
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();

        ServerBootstrap serverBootstrap = new ServerBootstrap();
        serverBootstrap
                .group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel nioSocketChannel) throws Exception {
                        nioSocketChannel.pipeline().addLast(new FirstServerHandler());
                    }
                })
                .bind(445);
    }

}

class FirstServerHandler extends ChannelInboundHandlerAdapter {

    // 接收到客户端的数据后被回调
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf byteBuf = (ByteBuf) msg;
        System.out.println(LocalDateTime.now() + ": 服务端读取到数据 -> " + byteBuf.toString(Charset.forName("utf-8")));

//        System.out.println(LocalDateTime.now() + ": 服务端发送数据");
//        ByteBuf outByteBuf = getByteBuf(ctx);
//        ctx.channel().writeAndFlush(outByteBuf);
    }

    private ByteBuf getByteBuf(ChannelHandlerContext ctx) {
        ByteBuf byteBuf = ctx.alloc().buffer();
        byteBuf.writeBytes("exercise1 complete".getBytes(Charset.forName("utf-8")));
        return byteBuf;
    }

}