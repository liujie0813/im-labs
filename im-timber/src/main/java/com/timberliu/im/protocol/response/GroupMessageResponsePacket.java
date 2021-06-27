package com.timberliu.im.protocol.response;

import com.timberliu.im.protocol.AbstractPacket;
import lombok.Data;

import static com.timberliu.im.protocol.command.Command.GROUP_MESSAGE_RESPONSE;
import static com.timberliu.im.protocol.command.Command.MESSAGE_RESPONSE;

/**
 * Created by liujie on 2021/6/22
 */
@Data
public class GroupMessageResponsePacket extends AbstractPacket {

    private boolean success;

    private String fromGroupId;

    private String fromUserName;

    private String message;

    private String reason;

    @Override
    public byte getCommand() {
        return GROUP_MESSAGE_RESPONSE;
    }
}
