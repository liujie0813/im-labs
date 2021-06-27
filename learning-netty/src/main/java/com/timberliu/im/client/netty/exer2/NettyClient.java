package com.timberliu.im.client.netty.exer2;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
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
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf byteBuf = (ByteBuf) msg;
        System.out.println(LocalDateTime.now() + ": 客户读取到数据 -> " + byteBuf.toString(Charset.forName("utf-8")));

        System.out.println(LocalDateTime.now() + ": 客户端发送数据");
        ByteBuf outByteBuf = getByteBuf(ctx);
        ctx.channel().writeAndFlush(outByteBuf);
    }

    private ByteBuf getByteBuf(ChannelHandlerContext ctx) {
        // alloc 获取一个 ByteBuf 的内存管理器，分配一个 ByteBuf
        ByteBuf byteBuf = ctx.alloc().buffer();
        byteBuf.writeBytes("exercise2 complete".getBytes(Charset.forName("utf-8")));
        return byteBuf;
    }

}
