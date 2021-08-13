package com.timberliu.im.client.handler;

import com.timberliu.im.client.NettyClient;
import com.timberliu.im.protocol.response.LoginResponsePacket;
import com.timberliu.im.session.Session;
import com.timberliu.im.util.SessionUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.time.LocalDateTime;

/**
 * Created by liujie on 2021/6/22
 */

public class LoginResponseHandler extends SimpleChannelInboundHandler<LoginResponsePacket> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, LoginResponsePacket loginResponsePacket) throws Exception {
        System.out.println("[Login] received login response【 " + loginResponsePacket + " 】");
        String userId = loginResponsePacket.getUserId();
        String username = loginResponsePacket.getUsername();

        if (loginResponsePacket.isSuccess()) {
            System.out.println("[Login] login success");
            SessionUtil.bindSession(new Session(userId, username), ctx.channel());
        } else {
            System.out.println("[Login] login fail，reason：" + loginResponsePacket.getReason());
        }
        NettyClient.nextCommand.compareAndSet(false, true);
    }
}
