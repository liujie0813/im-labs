package com.timberliu.im.client.handler;

import com.timberliu.im.client.NettyClient;
import com.timberliu.im.protocol.response.JoinGroupResponsePacket;
import com.timberliu.im.protocol.response.QuitGroupResponsePacket;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * Created by liujie on 2021/6/23
 */

public class QuitGroupResponseHandler extends SimpleChannelInboundHandler<QuitGroupResponsePacket> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, QuitGroupResponsePacket quitGroupResponsePacket) throws Exception {
        if (quitGroupResponsePacket.isSuccess()) {
            System.out.print("\n[QuitGroup] quit group success，groupId：【" + quitGroupResponsePacket.getGroupId() + " 】");
        } else {
            System.out.println("\n[QuitGroup] quit group failed");
        }
        NettyClient.nextCommand.compareAndSet(false, true);
    }
}
