package com.timberliu.im.server.handler;

import com.timberliu.im.protocol.request.CreateGroupRequestPacket;
import com.timberliu.im.protocol.request.JoinGroupRequestPacket;
import com.timberliu.im.protocol.response.CreateGroupResponsePacket;
import com.timberliu.im.protocol.response.JoinGroupResponsePacket;
import com.timberliu.im.session.Session;
import com.timberliu.im.util.IDUtil;
import com.timberliu.im.util.SessionUtil;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.ChannelMatcher;
import io.netty.channel.group.DefaultChannelGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by liujie on 2021/6/23
 */
@ChannelHandler.Sharable
public class JoinGroupRequestHandler extends SimpleChannelInboundHandler<JoinGroupRequestPacket> {

    public static final JoinGroupRequestHandler INSTANCE = new JoinGroupRequestHandler();

    protected JoinGroupRequestHandler() {}

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, JoinGroupRequestPacket joinGroupRequestPacket) throws Exception {
        System.out.println("\n[JoinGroup] received join group request【 " + joinGroupRequestPacket + " 】");
        String groupId = joinGroupRequestPacket.getGroupId();
        ChannelGroup channelGroup = SessionUtil.getChannelGroup(groupId);
        List<String> userNameList = new ArrayList<>();
        for (Channel channel : channelGroup) {
            userNameList.add(SessionUtil.getSession(channel).getUsername());
        }
        // 加入 channelGroup
        channelGroup.add(ctx.channel());

        JoinGroupResponsePacket joinGroupResponsePacket = new JoinGroupResponsePacket();
        joinGroupResponsePacket.setSuccess(true);
        joinGroupResponsePacket.setGroupId(groupId);
        joinGroupResponsePacket.setMessage("You've joined【 " + groupId + " 】group chat, There are " + userNameList + " in the group chat");

        Session session = SessionUtil.getSession(ctx.channel());
        System.out.println("[JoinGroup] send join group response【 " + joinGroupResponsePacket + " 】to【 " + session.getUsername() + " 】");
        ctx.channel().writeAndFlush(joinGroupResponsePacket);

        joinGroupResponsePacket.setMessage("【 " + session.getUsername() + " 】joined【 " + groupId + " 】group chat");
        System.out.println("[JoinGroup] send join group response【 " + joinGroupResponsePacket + " 】to " + userNameList);
        channelGroup.writeAndFlush(joinGroupResponsePacket, new ChannelMatcher() {
            @Override
            public boolean matches(Channel channel) {
                return channel != ctx.channel();
            }
        });
    }
}
