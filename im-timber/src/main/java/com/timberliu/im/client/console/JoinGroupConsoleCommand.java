package com.timberliu.im.client.console;

import com.timberliu.im.protocol.request.JoinGroupRequestPacket;
import io.netty.channel.Channel;

import java.util.Scanner;

/**
 * Created by liujie on 2021/6/23
 */

public class JoinGroupConsoleCommand implements ConsoleCommand {

    @Override
    public void exec(Scanner scanner, Channel channel) {
        System.out.print("[JoinGroup] Enter groupChatId > ");
        String groupId = scanner.next();

        JoinGroupRequestPacket joinGroupRequestPacket = new JoinGroupRequestPacket();
        joinGroupRequestPacket.setGroupId(groupId);
        System.out.println("[JoinGroup] Send join group request【" + joinGroupRequestPacket + "】");
        channel.writeAndFlush(joinGroupRequestPacket);
    }
}
