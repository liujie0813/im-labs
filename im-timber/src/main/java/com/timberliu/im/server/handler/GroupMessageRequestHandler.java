package com.timberliu.im.server.handler;

import com.timberliu.im.protocol.request.GroupMessageRequestPacket;
import com.timberliu.im.protocol.request.MessageRequestPacket;
import com.timberliu.im.protocol.response.GroupMessageResponsePacket;
import com.timberliu.im.protocol.response.MessageResponsePacket;
import com.timberliu.im.session.Session;
import com.timberliu.im.util.SessionUtil;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.ChannelMatcher;

/**
 * Created by liujie on 2021/6/22
 */
@ChannelHandler.Sharable
public class GroupMessageRequestHandler extends SimpleChannelInboundHandler<GroupMessageRequestPacket> {

    public static final GroupMessageRequestHandler INSTANCE = new GroupMessageRequestHandler();

    protected GroupMessageRequestHandler() {}

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, GroupMessageRequestPacket groupMessageRequestPacket) throws Exception {
        System.out.println("\n[SendToGroup] received send group message request【 " + groupMessageRequestPacket + " 】");

        String username = SessionUtil.getSession(ctx.channel()).getUsername();
        String toGroupId = groupMessageRequestPacket.getToGroupId();

        GroupMessageResponsePacket groupMessageResponsePacket = new GroupMessageResponsePacket();
        groupMessageResponsePacket.setFromGroupId(toGroupId);
        groupMessageResponsePacket.setFromUserName(username);
        groupMessageResponsePacket.setMessage(groupMessageRequestPacket.getMessage());

        // 通过消息接收方的标识获取对应点 channel
        ChannelGroup channelGroup = SessionUtil.getChannelGroup(toGroupId);
        System.out.println("[SendToGroup] " + username + " send message to group【 " + toGroupId + " 】");
        channelGroup.writeAndFlush(groupMessageResponsePacket, new ChannelMatcher() {
            @Override
            public boolean matches(Channel channel) {
                return ctx.channel() != channel;
            }
        });

        MessageResponsePacket fromResponsePacket = new MessageResponsePacket();
        fromResponsePacket.setSuccess(true);
        System.out.println("[SendToGroup] send message to group【 " + toGroupId + " 】success");
        ctx.channel().writeAndFlush(fromResponsePacket);
    }
}
