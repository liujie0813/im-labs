package com.timberliu.im.client.console;

import com.timberliu.im.protocol.request.JoinGroupRequestPacket;
import com.timberliu.im.protocol.request.ListGroupMembersRequestPacket;
import io.netty.channel.Channel;

import java.util.Scanner;

/**
 * Created by liujie on 2021/6/23
 */

public class ListGroupMembersConsoleCommand implements ConsoleCommand {

    @Override
    public void exec(Scanner scanner, Channel channel) {
        System.out.print("[ListGroupMembers] Enter groupChatId > ");
        String groupId = scanner.next();

        ListGroupMembersRequestPacket listGroupMembersRequestPacket = new ListGroupMembersRequestPacket();
        listGroupMembersRequestPacket.setGroupId(groupId);
        channel.writeAndFlush(listGroupMembersRequestPacket);
    }
}
