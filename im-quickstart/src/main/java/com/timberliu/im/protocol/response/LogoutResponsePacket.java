package com.timberliu.im.protocol.response;

import com.timberliu.im.protocol.AbstractPacket;
import lombok.Data;

import static com.timberliu.im.protocol.command.Command.CREATE_GROUP_REQUEST;
import static com.timberliu.im.protocol.command.Command.CREATE_GROUP_RESPONSE;
import static com.timberliu.im.protocol.command.Command.LOGOUT_RESPONSE;

/**
 * Created by liujie on 2021/6/23
 */
@Data
public class LogoutResponsePacket extends AbstractPacket {

    private boolean success;

    private String reason;

    @Override
    public byte getCommand() {
        return LOGOUT_RESPONSE;
    }
}
