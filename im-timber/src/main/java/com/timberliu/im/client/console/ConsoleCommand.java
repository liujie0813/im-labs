package com.timberliu.im.client.console;

import io.netty.channel.Channel;

import java.util.Scanner;

/**
 * Created by liujie on 2021/6/23
 */

public interface ConsoleCommand {

    void exec(Scanner scanner, Channel channel);
}
