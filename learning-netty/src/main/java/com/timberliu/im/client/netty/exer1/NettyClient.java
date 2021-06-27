package com.timberliu.im.client.netty.exer1;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.nio.charset.Charset;
import java.time.LocalDateTime;

/**
 * Created by liujie on 2021/6/21
 */

public class NettyClient {

    public static void main(String[] args) throws InterruptedException {
        Bootstrap bootstrap = new Bootstrap();
        NioEventLoopGroup group = new NioEventLoopGroup();

        bootstrap.group(group)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<Channel>() {
                    @Override
                    protected void initChannel(Channel channel) throws Exception {
                        channel.pipeline().addLast(new FirstClientHandler());
                    }
                })
                .connect("127.0.0.1", 445);
    }

}

class FirstClientHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println(LocalDateTime.now() + ": 客户端发送数据");
        for (int i = 0; i < 1000; i++) {
            ByteBuf byteBuf = getByteBuf(ctx);
            ctx.channel().writeAndFlush(byteBuf);
            Thread.sleep(0, 100);
        }
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf byteBuf = (ByteBuf) msg;
        System.out.println(LocalDateTime.now() + ": 客户读取到数据 -> " + byteBuf.toString(Charset.forName("utf-8")));
    }

    private ByteBuf getByteBuf(ChannelHandlerContext ctx) {
        // alloc 获取一个 ByteBuf 的内存管理器，分配一个 ByteBuf
        ByteBuf byteBuf = ctx.alloc().buffer();
        byteBuf.writeBytes("浮点数老费劲了快速减肥拉进来副科级ADSL咖啡机啊数据弗兰克斯极度空灵飞机撒拉飞机了".getBytes(Charset.forName("utf-8")));
        return byteBuf;
    }

}
