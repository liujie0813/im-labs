package com.timberliu.im.client.handler;

import com.timberliu.im.client.NettyClient;
import com.timberliu.im.protocol.response.JoinGroupResponsePacket;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * Created by liujie on 2021/6/23
 */

public class JoinGroupResponseHandler extends SimpleChannelInboundHandler<JoinGroupResponsePacket> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, JoinGroupResponsePacket joinGroupResponsePacket) throws Exception {
        System.out.print("\n[JoinGroup] join group sucess, " + joinGroupResponsePacket.getMessage());
        NettyClient.nextCommand.compareAndSet(false, true);
    }
}
