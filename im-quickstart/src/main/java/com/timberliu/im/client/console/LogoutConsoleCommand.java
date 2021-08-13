package com.timberliu.im.client.console;

import com.timberliu.im.protocol.request.LogoutRequestPacket;
import com.timberliu.im.session.Session;
import com.timberliu.im.util.SessionUtil;
import io.netty.channel.Channel;

import java.util.Scanner;

/**
 * Created by liujie on 2021/6/23
 */

public class LogoutConsoleCommand implements ConsoleCommand {

    @Override
    public void exec(Scanner scanner, Channel channel) {
        LogoutRequestPacket logoutRequestPacket = new LogoutRequestPacket();
        Session session = SessionUtil.getSession(channel);
        logoutRequestPacket.setUsername(session.getUsername());
        logoutRequestPacket.setPassword("password");
        System.out.println("[Logout] send logout request【" + logoutRequestPacket + " 】");
        channel.writeAndFlush(logoutRequestPacket);
    }
}
