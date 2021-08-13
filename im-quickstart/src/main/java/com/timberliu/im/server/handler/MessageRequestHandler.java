package com.timberliu.im.server.handler;

import com.timberliu.im.protocol.request.MessageRequestPacket;
import com.timberliu.im.protocol.response.MessageResponsePacket;
import com.timberliu.im.session.Session;
import com.timberliu.im.util.SessionUtil;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.time.LocalDateTime;

/**
 * Created by liujie on 2021/6/22
 */
@ChannelHandler.Sharable
public class MessageRequestHandler extends SimpleChannelInboundHandler<MessageRequestPacket> {

    public static final MessageRequestHandler INSTANCE = new MessageRequestHandler();

    protected MessageRequestHandler() {}

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, MessageRequestPacket messageRequestPacket) throws Exception {
        System.out.println("\n[SendToUser] received send message request【 " + messageRequestPacket + " 】");

        Session session = SessionUtil.getSession(ctx.channel());

        MessageResponsePacket toResponsePacket = new MessageResponsePacket();
        toResponsePacket.setFromUserId(session.getUserId());
        toResponsePacket.setFromUserName(session.getUsername());

        MessageResponsePacket fromResponsePacket = new MessageResponsePacket();

        // 通过消息接收方的标识获取对应点 channel
        Channel toUserChannel = SessionUtil.getChannel(messageRequestPacket.getToUserId());
        if (toUserChannel != null && SessionUtil.hasLogin(toUserChannel)) {
            toResponsePacket.setMessage(messageRequestPacket.getMessage());
            System.out.println("[SendToUser]【 " + messageRequestPacket.getToUserId() + " 】online，send message success");
            toUserChannel.writeAndFlush(toResponsePacket);

            fromResponsePacket.setSuccess(true);
        } else {
            System.out.println("[SendToUser]【 " + messageRequestPacket.getToUserId() + " 】offline，waiting for send");

            fromResponsePacket.setSuccess(false);
            fromResponsePacket.setReason("【 " + messageRequestPacket.getToUserId() + " 】offline");
        }
        ctx.channel().writeAndFlush(fromResponsePacket);
    }
}
