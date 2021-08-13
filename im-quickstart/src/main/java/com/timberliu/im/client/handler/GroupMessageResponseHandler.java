package com.timberliu.im.client.handler;

import com.timberliu.im.client.NettyClient;
import com.timberliu.im.protocol.request.GroupMessageRequestPacket;
import com.timberliu.im.protocol.response.GroupMessageResponsePacket;
import com.timberliu.im.protocol.response.MessageResponsePacket;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.apache.commons.lang3.StringUtils;

/**
 * Created by liujie on 2021/6/22
 */

public class GroupMessageResponseHandler extends SimpleChannelInboundHandler<GroupMessageResponsePacket> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, GroupMessageResponsePacket groupMessageResponsePacket) throws Exception {
        System.out.println("\n[SendToGroup] received send group message response【 " + groupMessageResponsePacket + " 】");
        if (StringUtils.isNotBlank(groupMessageResponsePacket.getMessage())) {
            System.out.println("[SendToGroup]【 " + groupMessageResponsePacket.getFromUserName() + " 】send【 " + groupMessageResponsePacket.getMessage() + " 】to group【" + groupMessageResponsePacket.getFromGroupId() + " 】" );
        } else {
            System.out.println("[SendToGroup] send group message " + (groupMessageResponsePacket.isSuccess() ? "success" : "fail"));
        }
        NettyClient.nextCommand.compareAndSet(false, true);
    }
}
