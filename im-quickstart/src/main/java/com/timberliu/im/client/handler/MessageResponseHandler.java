package com.timberliu.im.client.handler;

import com.timberliu.im.client.NettyClient;
import com.timberliu.im.protocol.response.MessageResponsePacket;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.apache.commons.lang3.StringUtils;

import java.time.LocalDateTime;

/**
 * Created by liujie on 2021/6/22
 */

public class MessageResponseHandler extends SimpleChannelInboundHandler<MessageResponsePacket> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, MessageResponsePacket messageResponsePacket) throws Exception {
        System.out.println("\n[SendToUser] received send message response【 " + messageResponsePacket + " 】");
        if (StringUtils.isNotBlank(messageResponsePacket.getMessage())) {
            System.out.println("[SendToUser]【 " + messageResponsePacket.getFromUserName() + " 】say【 " + messageResponsePacket.getMessage() + " 】to you" );
        } else {
            System.out.println("[SendToUser] send message " + (messageResponsePacket.isSuccess() ? "success" : "fail"));
        }
        NettyClient.nextCommand.compareAndSet(false, true);
    }
}
