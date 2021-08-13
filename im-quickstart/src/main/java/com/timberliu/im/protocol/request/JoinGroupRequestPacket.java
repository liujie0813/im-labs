package com.timberliu.im.protocol.request;

import com.timberliu.im.protocol.AbstractPacket;
import lombok.Data;

import static com.timberliu.im.protocol.command.Command.JOIN_GROUP_REQUEST;

/**
 * Created by liujie on 2021/6/23
 */
@Data
public class JoinGroupRequestPacket extends AbstractPacket {

    private String groupId;

    @Override
    public byte getCommand() {
        return JOIN_GROUP_REQUEST;
    }
}
