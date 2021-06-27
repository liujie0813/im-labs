package com.timberliu.im.protocol.response;

import com.timberliu.im.protocol.AbstractPacket;
import lombok.Data;

import static com.timberliu.im.protocol.command.Command.QUIT_GROUP_RESPONSE;

/**
 * Created by liujie on 2021/6/23
 */
@Data
public class QuitGroupResponsePacket extends AbstractPacket {

    private boolean success;

    private String groupId;

    @Override
    public byte getCommand() {
        return QUIT_GROUP_RESPONSE;
    }
}
