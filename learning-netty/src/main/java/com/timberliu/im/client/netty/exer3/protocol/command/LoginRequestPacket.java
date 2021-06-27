package com.timberliu.im.client.netty.exer3.protocol.command;

import lombok.Data;

import static com.timberliu.im.client.netty.exer3.protocol.command.Command.LOGIN_REQUEST;

/**
 * Created by liujie on 2021/6/22
 */
@Data
public class LoginRequestPacket extends AbstractPacket {

    private Integer userId;

    private String username;

    private String password;

    @Override
    public byte getCommand() {
        return LOGIN_REQUEST;
    }
}
