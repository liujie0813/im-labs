package com.timberliu.im.server.handler;

import com.timberliu.im.protocol.request.LoginRequestPacket;
import com.timberliu.im.protocol.response.LoginResponsePacket;
import com.timberliu.im.session.Session;
import com.timberliu.im.util.IDUtil;
import com.timberliu.im.util.SessionUtil;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * Created by liujie on 2021/6/22
 */
// 支持多个 handler 共享
@ChannelHandler.Sharable
public class LoginRequestHandler extends SimpleChannelInboundHandler<LoginRequestPacket> {

    // 单例模式，多个 handler 共享，不用每次来新连接，创建新实例
    public static final LoginRequestHandler INSTANCE = new LoginRequestHandler();

    protected LoginRequestHandler() {}

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, LoginRequestPacket loginRequestPacket) throws Exception {
        ctx.channel().writeAndFlush(login(ctx, loginRequestPacket));
    }

    private LoginResponsePacket login(ChannelHandlerContext ctx, LoginRequestPacket loginRequestPacket) {
        System.out.println("\n[Login] received login request【 " + loginRequestPacket + " 】");

        LoginResponsePacket loginResponsePacket = new LoginResponsePacket();
        loginResponsePacket.setVersion(loginRequestPacket.getVersion());
        loginResponsePacket.setUsername(loginRequestPacket.getUsername());

        if (valid(loginRequestPacket)) {
            // 成功
            loginResponsePacket.setSuccess(true);
            String userId = IDUtil.randomId();
            loginResponsePacket.setUserId(userId);
            SessionUtil.bindSession(new Session(userId, loginRequestPacket.getUsername()), ctx.channel());
            System.out.println("[Login]【" + loginRequestPacket.getUsername() + "】login success");
        } else {
            // 失败
            loginResponsePacket.setSuccess(false);
            loginResponsePacket.setReason("username/password valid failed");
            System.out.println("[Login]【" + loginRequestPacket.getUsername() + " 】login fail");
        }
        System.out.println("[Login] send login response【 " + loginResponsePacket + " 】");
        return loginResponsePacket;
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        SessionUtil.unBindSession(ctx.channel());
    }

    private boolean valid(LoginRequestPacket loginRequestPacket) {
        System.out.println("[Login] valid success");
        return true;
    }
}
