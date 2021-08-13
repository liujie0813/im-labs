package com.timberliu.im.protocol.request;

import com.timberliu.im.protocol.AbstractPacket;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import static com.timberliu.im.protocol.command.Command.GROUP_MESSAGE_REQUEST;
import static com.timberliu.im.protocol.command.Command.MESSAGE_REQUEST;

/**
 * Created by liujie on 2021/6/22
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GroupMessageRequestPacket extends AbstractPacket {

    private String toGroupId;

    private String message;

    @Override
    public byte getCommand() {
        return GROUP_MESSAGE_REQUEST;
    }
}
