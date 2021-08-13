package com.timberliu.im.client.handler;

import com.timberliu.im.client.NettyClient;
import com.timberliu.im.protocol.response.LogoutResponsePacket;
import com.timberliu.im.util.SessionUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * Created by liujie on 2021/6/23
 */

public class LogoutReponseHandler extends SimpleChannelInboundHandler<LogoutResponsePacket> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, LogoutResponsePacket logoutResponsePacket) throws Exception {
        System.out.println("\n[Logout] received logout response【 " + logoutResponsePacket + " 】");
        SessionUtil.unBindSession(ctx.channel());
        NettyClient.nextCommand.compareAndSet(false, true);
    }
}
