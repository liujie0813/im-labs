package com.timberliu.im.client.handler;

import com.timberliu.im.client.NettyClient;
import com.timberliu.im.protocol.response.CreateGroupResponsePacket;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * Created by liujie on 2021/6/23
 */

public class CreateGroupResponseHandler extends SimpleChannelInboundHandler<CreateGroupResponsePacket> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, CreateGroupResponsePacket createGroupResponsePacket) throws Exception {
        System.out.print("\n[CreateGroup] create group success，There are " + createGroupResponsePacket.getUserNameList() +
                " in the group【 " + createGroupResponsePacket.getGroupId() + " 】");
        NettyClient.nextCommand.compareAndSet(false, true);
    }
}
