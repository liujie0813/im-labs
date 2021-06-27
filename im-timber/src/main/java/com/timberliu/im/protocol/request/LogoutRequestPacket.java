package com.timberliu.im.protocol.request;

import com.timberliu.im.protocol.AbstractPacket;
import lombok.Data;

import static com.timberliu.im.protocol.command.Command.CREATE_GROUP_REQUEST;
import static com.timberliu.im.protocol.command.Command.LOGOUT_REQUEST;

/**
 * Created by liujie on 2021/6/23
 */
@Data
public class LogoutRequestPacket extends AbstractPacket {

    private String username;

    private String password;

    @Override
    public byte getCommand() {
        return LOGOUT_REQUEST;
    }
}
