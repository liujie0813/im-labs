package com.timberliu.im.client.console;

import com.timberliu.im.protocol.request.LoginRequestPacket;
import io.netty.channel.Channel;

import java.io.Console;
import java.time.LocalDateTime;
import java.util.Scanner;

/**
 * Created by liujie on 2021/6/23
 */

public class LoginConsoleCommand implements ConsoleCommand {

    @Override
    public void exec(Scanner scanner, Channel channel) {
        System.out.print("[Login] Enter username > ");
        String nextLine = scanner.nextLine();

        LoginRequestPacket loginRequestPacket = new LoginRequestPacket();
        loginRequestPacket.setUsername(nextLine);
        loginRequestPacket.setPassword("password");

        System.out.println("[Login] send login request【 " + loginRequestPacket + " 】");
        channel.writeAndFlush(loginRequestPacket);
        waitForLoginResponse();
    }

    private static void waitForLoginResponse() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
