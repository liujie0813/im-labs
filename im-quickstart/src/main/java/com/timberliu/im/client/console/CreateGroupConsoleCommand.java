package com.timberliu.im.client.console;

import com.timberliu.im.protocol.request.CreateGroupRequestPacket;
import io.netty.channel.Channel;

import java.util.Arrays;
import java.util.Scanner;

/**
 * Created by liujie on 2021/6/23
 */

public class CreateGroupConsoleCommand implements ConsoleCommand {

    private static final String USER_ID_SPLITER = ",";

    @Override
    public void exec(Scanner scanner, Channel channel) {
        CreateGroupRequestPacket createGroupRequestPacket = new CreateGroupRequestPacket();
        System.out.print("[CreateGroup] Enter userId list > ");
        String userIds = scanner.next();
        createGroupRequestPacket.setUserIdList(Arrays.asList(userIds.split(USER_ID_SPLITER)));
        System.out.println("[CreateGroup] Send create group request【" + createGroupRequestPacket + "】");
        channel.writeAndFlush(createGroupRequestPacket);
    }
}
