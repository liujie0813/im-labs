package com.timberliu.im.client.console;

import io.netty.channel.Channel;

import javax.print.attribute.standard.JobOriginatingUserName;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import static com.timberliu.im.client.console.ConsoleCommandEnum.*;

/**
 * Created by liujie on 2021/6/23
 */

public class ConsoleCommandManager implements ConsoleCommand {

    private Map<Integer, ConsoleCommand> consoleCommandMap;

    public ConsoleCommandManager() {
        consoleCommandMap = new HashMap<>();
        consoleCommandMap.put(SEND_TO_USER.getCode(), new SendToUserConsoleCommand());
        consoleCommandMap.put(LOGOUT.getCode(), new LogoutConsoleCommand());
        consoleCommandMap.put(CREATE_GROUP.getCode(), new CreateGroupConsoleCommand());
        consoleCommandMap.put(JOIN_GROUP.getCode(), new JoinGroupConsoleCommand());
        consoleCommandMap.put(QUIT_GROUP.getCode(), new QuitGroupConsoleCommand());
        consoleCommandMap.put(LIST_GROUP_MEMBERS.getCode(), new ListGroupMembersConsoleCommand());
        consoleCommandMap.put(SEND_TO_GROUP.getCode(), new SendToGroupConsoleCommand());
    }

    @Override
    public void exec(Scanner scanner, Channel channel) {
        StringBuilder sb = new StringBuilder("\nCommand list：");
        ConsoleCommandEnum[] values = ConsoleCommandEnum.values();
        for (int i = 0; i < values.length; i++) {
            sb.append("\n\t").append(i + 1).append(". ").append(values[i].getContent());
        }
        sb.append("\nEnter next command number (eg: 1) > ");
        System.out.print(sb.toString());
        Integer command = scanner.nextInt();
        ConsoleCommand consoleCommand = consoleCommandMap.get(command);
        if (consoleCommand != null) {
            consoleCommand.exec(scanner, channel);
        } else {
            System.out.println("Not identify [" + command + "] command，please enter again");
        }
    }
}
