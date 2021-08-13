package com.timberliu.im.protocol.request;

import com.timberliu.im.protocol.AbstractPacket;
import lombok.Data;

import static com.timberliu.im.protocol.command.Command.LOGIN_REQUEST;

/**
 * Created by liujie on 2021/6/22
 */
@Data
public class LoginRequestPacket extends AbstractPacket {

    private String userId;

    private String username;

    private String password;

    @Override
    public byte getCommand() {
        return LOGIN_REQUEST;
    }
}
