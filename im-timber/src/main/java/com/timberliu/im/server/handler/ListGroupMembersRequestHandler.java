package com.timberliu.im.server.handler;

import com.timberliu.im.protocol.request.JoinGroupRequestPacket;
import com.timberliu.im.protocol.request.ListGroupMembersRequestPacket;
import com.timberliu.im.protocol.response.JoinGroupResponsePacket;
import com.timberliu.im.protocol.response.ListGroupMembersResponsePacket;
import com.timberliu.im.session.Session;
import com.timberliu.im.util.SessionUtil;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by liujie on 2021/6/23
 */
@ChannelHandler.Sharable
public class ListGroupMembersRequestHandler extends SimpleChannelInboundHandler<ListGroupMembersRequestPacket> {

    public static final ListGroupMembersRequestHandler INSTANCE = new ListGroupMembersRequestHandler();

    protected ListGroupMembersRequestHandler() {}

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ListGroupMembersRequestPacket listGroupMembersRequestPacket) throws Exception {
        System.out.println("\n[listGroupMembers] received list group member request【" + listGroupMembersRequestPacket + " 】");
        String groupId = listGroupMembersRequestPacket.getGroupId();
        ChannelGroup channelGroup = SessionUtil.getChannelGroup(groupId);

        List<Session> sessionList = new ArrayList<>();
        List<String> userNameList = new ArrayList<>();
        for (Channel channel : channelGroup) {
            Session session = SessionUtil.getSession(channel);
            sessionList.add(session);
            userNameList.add(session.getUsername());
        }
        System.out.println("[listGroupMembers] group members: " + userNameList);

        ListGroupMembersResponsePacket listGroupMembersResponsePacket = new ListGroupMembersResponsePacket();
        listGroupMembersResponsePacket.setGroupId(groupId);
        listGroupMembersResponsePacket.setSessionList(sessionList);
        System.out.println("[listGroupMembers] send list group member response【" + listGroupMembersResponsePacket + " 】");
        ctx.channel().writeAndFlush(listGroupMembersResponsePacket);
    }
}
