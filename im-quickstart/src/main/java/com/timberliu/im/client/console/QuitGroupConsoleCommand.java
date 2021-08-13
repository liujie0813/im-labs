package com.timberliu.im.client.console;

import com.timberliu.im.protocol.request.JoinGroupRequestPacket;
import com.timberliu.im.protocol.request.QuitGroupRequestPacket;
import io.netty.channel.Channel;

import java.util.Scanner;

/**
 * Created by liujie on 2021/6/23
 */

public class QuitGroupConsoleCommand implements ConsoleCommand {

    @Override
    public void exec(Scanner scanner, Channel channel) {
        System.out.print("[QuitGroup] Enter groupChatId > ");
        String groupId = scanner.next();

        QuitGroupRequestPacket quitGroupRequestPacket = new QuitGroupRequestPacket();
        quitGroupRequestPacket.setGroupId(groupId);
        System.out.println("[QuitGroup] send quit group request【" + quitGroupRequestPacket + "】");
        channel.writeAndFlush(quitGroupRequestPacket);
    }
}
