package com.timberliu.im.server.handler;

import com.timberliu.im.protocol.AbstractPacket;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.HashMap;
import java.util.Map;

import static com.timberliu.im.protocol.command.Command.*;

/**
 * Created by liujie on 2021/6/24
 */
@ChannelHandler.Sharable
public class IMHandler extends SimpleChannelInboundHandler<AbstractPacket> {

    public static final IMHandler INSTANCE = new IMHandler();

    private static Map<Byte, SimpleChannelInboundHandler<? extends AbstractPacket>> handlerMap;

    static {
        handlerMap = new HashMap<>();

        handlerMap.put(MESSAGE_REQUEST, MessageRequestHandler.INSTANCE);
        handlerMap.put(LOGOUT_REQUEST, CreateGroupRequestHandler.INSTANCE);
        handlerMap.put(CREATE_GROUP_REQUEST, GroupMessageRequestHandler.INSTANCE);
        handlerMap.put(JOIN_GROUP_REQUEST, JoinGroupRequestHandler.INSTANCE);
        handlerMap.put(QUIT_GROUP_REQUEST, QuitGroupRequestHandler.INSTANCE);
        handlerMap.put(LIST_GROUP_MEMBERS_REQUEST, ListGroupMembersRequestHandler.INSTANCE);
        handlerMap.put(GROUP_MESSAGE_REQUEST, LogoutRequestHandler.INSTANCE);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, AbstractPacket abstractPacket) throws Exception {
        handlerMap.get(abstractPacket.getCommand()).channelRead(ctx, abstractPacket);
    }
}
