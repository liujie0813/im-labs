package com.timberliu.im.client.handler;

import com.timberliu.im.client.NettyClient;
import com.timberliu.im.protocol.response.JoinGroupResponsePacket;
import com.timberliu.im.protocol.response.ListGroupMembersResponsePacket;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * Created by liujie on 2021/6/23
 */

public class ListGroupMembersResponseHandler extends SimpleChannelInboundHandler<ListGroupMembersResponsePacket> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ListGroupMembersResponsePacket listGroupMembersResponsePacket) throws Exception {
        System.out.print("\n[ListGroupMembers] There are " + listGroupMembersResponsePacket.getSessionList() + " in the group chat【 " + listGroupMembersResponsePacket.getGroupId() + " 】");
        NettyClient.nextCommand.compareAndSet(false, true);
    }
}
