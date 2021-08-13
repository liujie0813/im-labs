package com.timberliu.im.client.netty.wesocket;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.concurrent.GlobalEventExecutor;

import java.time.LocalDateTime;

/**
 * 处理消息
 * TextWebSocketFrame：用于专门处理 websocket 的文本对象
 *
 * Created by liujie on 2021/7/12
 */

public class ChatHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {

    /**
     * 记录所有客户端
     */
    private static ChannelGroup channels = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame frame) throws Exception {
        // 获取客户端传递过来的消息
        String content = frame.text();
        System.out.println(content);

        channels.writeAndFlush(new TextWebSocketFrame("[服务器在【 " + LocalDateTime.now() + " 】接收到【 " + content + " 】"));
    }

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        channels.add(ctx.channel());
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
//        channels.remove(ctx.channel());
        System.out.println("[handlerRemoved] " + ctx.channel().id().asLongText());
    }

}
