package com.timberliu.im.protocol.request;

import com.timberliu.im.protocol.AbstractPacket;
import lombok.Data;

import static com.timberliu.im.protocol.command.Command.LIST_GROUP_MEMBERS_REQUEST;
import static com.timberliu.im.protocol.command.Command.QUIT_GROUP_REQUEST;

/**
 * Created by liujie on 2021/6/23
 */
@Data
public class ListGroupMembersRequestPacket extends AbstractPacket {

    private String groupId;

    @Override
    public byte getCommand() {
        return LIST_GROUP_MEMBERS_REQUEST;
    }
}
