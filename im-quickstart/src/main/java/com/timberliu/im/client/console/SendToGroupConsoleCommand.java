package com.timberliu.im.client.console;

import com.timberliu.im.protocol.request.GroupMessageRequestPacket;
import com.timberliu.im.protocol.request.MessageRequestPacket;
import io.netty.channel.Channel;

import java.util.Scanner;

/**
 * Created by liujie on 2021/6/23
 */

public class SendToGroupConsoleCommand implements ConsoleCommand {

    @Override
    public void exec(Scanner scanner, Channel channel) {
        System.out.print("[SendToGroup] Enter the groupId > ");
        String toGroupId = scanner.next();
        System.out.print("[SendToGroup] Enter message > ");
        String message = scanner.next();
        GroupMessageRequestPacket groupMessageRequestPacket = new GroupMessageRequestPacket();
        groupMessageRequestPacket.setToGroupId(toGroupId);
        groupMessageRequestPacket.setMessage(message);
        System.out.println("[SendToGroup] send message【 " + groupMessageRequestPacket + " 】");
        channel.writeAndFlush(groupMessageRequestPacket);
    }
}
