package com.timberliu.im.protocol.response;

import com.timberliu.im.protocol.AbstractPacket;
import lombok.Data;

import static com.timberliu.im.protocol.command.Command.LOGIN_RESPONSE;

/**
 * Created by liujie on 2021/6/22
 */
@Data
public class LoginResponsePacket extends AbstractPacket {

    private String userId;

    private String username;

    private boolean success;

    private String reason;

    @Override
    public byte getCommand() {
        return LOGIN_RESPONSE;
    }
}
