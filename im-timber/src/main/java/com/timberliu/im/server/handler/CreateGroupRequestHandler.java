package com.timberliu.im.server.handler;

import com.timberliu.im.protocol.request.CreateGroupRequestPacket;
import com.timberliu.im.protocol.response.CreateGroupResponsePacket;
import com.timberliu.im.util.IDUtil;
import com.timberliu.im.util.SessionUtil;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by liujie on 2021/6/23
 */
@ChannelHandler.Sharable
public class CreateGroupRequestHandler extends SimpleChannelInboundHandler<CreateGroupRequestPacket> {

    public static final CreateGroupRequestHandler INSTANCE = new CreateGroupRequestHandler();

    protected CreateGroupRequestHandler() {}

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, CreateGroupRequestPacket createGroupRequestPacket) throws Exception {
        System.out.println("\n[CreateGroup] received create group request【 " + createGroupRequestPacket + " 】");
        List<String> userIdList = createGroupRequestPacket.getUserIdList();

        // DefaultChannelGroup：对 channel 进行批量读写
        DefaultChannelGroup channelGroup = new DefaultChannelGroup(ctx.executor());
        List<String> userNameList = new ArrayList<>();
        for (String userId : userIdList) {
            Channel channel = SessionUtil.getChannel(userId);
            if (channel != null) {
                channelGroup.add(channel);
                userNameList.add(SessionUtil.getSession(channel).getUsername());
            }
        }
        String groupId = IDUtil.randomId();

        System.out.println("[CreateGroup] create group success, groupId:【 " + groupId + " 】, group member：" + userNameList);
        SessionUtil.bindChannelGroup(groupId, channelGroup);

        CreateGroupResponsePacket createGroupResponsePacket = new CreateGroupResponsePacket();
        createGroupResponsePacket.setSuccess(true);
        createGroupResponsePacket.setGroupId(groupId);
        createGroupResponsePacket.setUserNameList(userNameList);
        System.out.println("[Create group] send create group response【 " + createGroupResponsePacket + " 】");
        // 给每个客户端发送 群聊建立 通知
        channelGroup.writeAndFlush(createGroupResponsePacket);
    }
}
