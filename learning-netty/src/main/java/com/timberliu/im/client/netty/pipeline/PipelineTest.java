package com.timberliu.im.client.netty.pipeline;

import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.util.Attribute;
import io.netty.util.AttributeKey;

import java.time.LocalDateTime;

/**
 * Created by liujie on 2021/6/22
 */

public class PipelineTest {

    public static void main(String[] args) throws Exception {
        NioEventLoopGroup bossGroup = new NioEventLoopGroup();
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();

        ServerBootstrap serverBootstrap = new ServerBootstrap();
        serverBootstrap.group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel ch) throws Exception {
                        ch.pipeline().addLast("inA", new InBoundHandlerA());
                        ch.pipeline().addLast("inB", new InBoundHandlerB());
                        ch.pipeline().addLast("inC", new InBoundHandlerC());

                        ch.pipeline().addLast("outA", new OutBoundHandlerA());
                        ch.pipeline().addLast("outB", new OutBoundHandlerB());
                        ch.pipeline().addLast("outC", new OutBoundHandlerC());
                    }
                })
                .bind(10000);
        Thread.sleep(3000);

        Bootstrap bootstrap = new Bootstrap();
        Channel channel = bootstrap.group(new NioEventLoopGroup())
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<Channel>() {
                    @Override
                    protected void initChannel(Channel ch) throws Exception {
                        ch.pipeline().addLast(new StringEncoder());
                    }
                })
                .connect("127.0.0.1", 10000).sync().channel();

        channel.writeAndFlush("hello");
    }
}

class InBoundHandlerA extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println(LocalDateTime.now() + " [InBoundHandlerA] " + msg);
        ctx.pipeline().remove("inB");
        ctx.pipeline().remove("outC");
        ctx.channel().attr(AttributeKey.newInstance("handleTime")).set(LocalDateTime.now());

        Thread.sleep(1000);
        super.channelRead(ctx, msg);
    }
}

class InBoundHandlerB extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        Attribute<Object> handleTime = ctx.channel().attr(AttributeKey.valueOf("handleTime"));
        System.out.println(handleTime.get() + " [InBoundHandlerB] last handle time:");
        handleTime.set(LocalDateTime.now());

        System.out.println(LocalDateTime.now() + " [InBoundHandlerB] " + msg);

        Thread.sleep(1000);
        super.channelRead(ctx, msg);
    }
}

class InBoundHandlerC extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        Attribute<Object> handleTime = ctx.channel().attr(AttributeKey.valueOf("handleTime"));
        System.out.println(handleTime.get() + " [InBoundHandlerC] last handle time");
        handleTime.set(LocalDateTime.now());

        System.out.println(LocalDateTime.now() + " [InBoundHandlerC] " + msg);

        // 向客户端写入数据，才会调用 OutBound
        Thread.sleep(1000);
        ctx.channel().writeAndFlush(msg);
    }
}

class OutBoundHandlerA extends ChannelOutboundHandlerAdapter {
    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        Attribute<Object> handleTime = ctx.channel().attr(AttributeKey.valueOf("handleTime"));
        System.out.println(handleTime.get() + " [OutBoundHandlerA] last handle time");
        handleTime.set(LocalDateTime.now());

        System.out.println(LocalDateTime.now() + " [OutBoundHandlerA] " + msg);

        Thread.sleep(1000);
        super.write(ctx, msg, promise);
    }
}

class OutBoundHandlerB extends ChannelOutboundHandlerAdapter {
    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        Attribute<Object> handleTime = ctx.channel().attr(AttributeKey.valueOf("handleTime"));
        System.out.println(handleTime.get() + " [OutBoundHandlerB] last handle time");
        handleTime.set(LocalDateTime.now());

        System.out.println(LocalDateTime.now() + " [OutBoundHandlerB] " + msg);

        Thread.sleep(1000);
        super.write(ctx, msg, promise);
    }
}

class OutBoundHandlerC extends ChannelOutboundHandlerAdapter {
    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        Attribute<Object> handleTime = ctx.channel().attr(AttributeKey.valueOf("handleTime"));
        System.out.println(handleTime.get() + " [OutBoundHandlerC] last handle time");
        handleTime.set(LocalDateTime.now());

        System.out.println(LocalDateTime.now() + " [OutBoundHandlerC] " + msg);

        Thread.sleep(1000);
        super.write(ctx, msg, promise);
    }
}