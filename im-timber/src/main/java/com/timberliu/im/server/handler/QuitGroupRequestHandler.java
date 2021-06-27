package com.timberliu.im.server.handler;

import com.timberliu.im.protocol.request.CreateGroupRequestPacket;
import com.timberliu.im.protocol.request.QuitGroupRequestPacket;
import com.timberliu.im.protocol.response.CreateGroupResponsePacket;
import com.timberliu.im.protocol.response.QuitGroupResponsePacket;
import com.timberliu.im.session.Session;
import com.timberliu.im.util.IDUtil;
import com.timberliu.im.util.SessionUtil;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by liujie on 2021/6/23
 */
@ChannelHandler.Sharable
public class QuitGroupRequestHandler extends SimpleChannelInboundHandler<QuitGroupRequestPacket> {

    public static final QuitGroupRequestHandler INSTANCE = new QuitGroupRequestHandler();

    protected QuitGroupRequestHandler() {}

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, QuitGroupRequestPacket quitGroupRequestPacket) throws Exception {
        System.out.println("\n[QuitGroup] received quit group request【 " + quitGroupRequestPacket + " 】");
        String groupId = quitGroupRequestPacket.getGroupId();
        ChannelGroup channelGroup = SessionUtil.getChannelGroup(groupId);
        channelGroup.remove(ctx.channel());

        List<String> userNameList = new ArrayList<>();
        for (Channel channel : channelGroup) {
            Session session = SessionUtil.getSession(channel);
            userNameList.add(session.getUsername());
        }
        System.out.println("[QuitGroup] group member list：" + userNameList);

        if (channelGroup.size() <= 0) {
            channelGroup.close();
            SessionUtil.unBindChannelGroup(groupId);
            System.out.println("[QuitGroup] group members less than 0，remove group【" + groupId + " 】");
        }

        QuitGroupResponsePacket quitGroupResponsePacket = new QuitGroupResponsePacket();
        quitGroupResponsePacket.setSuccess(true);
        quitGroupResponsePacket.setGroupId(groupId);
        System.out.println("[QuitGroup] send quit group response【 " + quitGroupResponsePacket + " 】");
        ctx.channel().writeAndFlush(quitGroupResponsePacket);
    }
}
