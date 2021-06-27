package com.timberliu.im.server.handler;

import com.timberliu.im.util.SessionUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * Created by liujie on 2021/6/23
 */

public class AuthHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (!SessionUtil.hasLogin(ctx.channel())) {
            // 直接关闭 channel，后续会依次执行 channelReadComplete、handlerInactive、handlerUnregistered、handlerRemoved
            ctx.channel().close();
        } else {
            // 移除，后续只会执行 handlerRemoved
            ctx.pipeline().remove(this);
            super.channelRead(ctx, msg);
        }
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        if (SessionUtil.hasLogin(ctx.channel())) {
            System.out.println("valid success, remove AuthHandler");
        } else {
            System.out.println("valid fail, close connection");
        }
    }
}
