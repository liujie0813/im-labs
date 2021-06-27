package com.timberliu.im.protocol.response;

import com.timberliu.im.protocol.AbstractPacket;
import lombok.Data;

import static com.timberliu.im.protocol.command.Command.JOIN_GROUP_RESPONSE;

/**
 * Created by liujie on 2021/6/23
 */
@Data
public class JoinGroupResponsePacket extends AbstractPacket {

    private boolean success;

    private String groupId;

    private String message;

    @Override
    public byte getCommand() {
        return JOIN_GROUP_RESPONSE;
    }
}
