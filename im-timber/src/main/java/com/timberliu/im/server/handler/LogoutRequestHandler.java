package com.timberliu.im.server.handler;

import com.timberliu.im.protocol.request.LogoutRequestPacket;
import com.timberliu.im.protocol.response.LogoutResponsePacket;
import com.timberliu.im.util.SessionUtil;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * Created by liujie on 2021/6/23
 */
@ChannelHandler.Sharable
public class LogoutRequestHandler extends SimpleChannelInboundHandler<LogoutRequestPacket> {

    public static final LogoutRequestHandler INSTANCE = new LogoutRequestHandler();

    protected LogoutRequestHandler() {}

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, LogoutRequestPacket logoutRequestPacket) throws Exception {
        System.out.println("\n[Logout] received logout request【 " + logoutRequestPacket + " 】");
        SessionUtil.unBindSession(ctx.channel());

        LogoutResponsePacket logoutResponsePacket = new LogoutResponsePacket();
        logoutResponsePacket.setSuccess(true);
        System.out.println("[Logout] send logout response【 " + logoutResponsePacket + " 】");
        ctx.channel().writeAndFlush(logoutResponsePacket);
    }
}
