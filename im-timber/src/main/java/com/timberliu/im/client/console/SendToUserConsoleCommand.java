package com.timberliu.im.client.console;

import com.timberliu.im.protocol.request.MessageRequestPacket;
import io.netty.channel.Channel;

import java.time.LocalDateTime;
import java.util.Scanner;

/**
 * Created by liujie on 2021/6/23
 */

public class SendToUserConsoleCommand implements ConsoleCommand {

    @Override
    public void exec(Scanner scanner, Channel channel) {
        System.out.print("[SendToUser] Enter the userId of the recipient > ");
        String toUserId = scanner.next();
        System.out.print("[SendToUser] Enter message > ");
        String message = scanner.next();
        MessageRequestPacket messageRequestPacket = new MessageRequestPacket(toUserId, message);
        System.out.println("[SendToUser] send message【 " + messageRequestPacket + " 】");
        channel.writeAndFlush(messageRequestPacket);
    }
}
