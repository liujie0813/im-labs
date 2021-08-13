package com.timberliu.im.protocol.response;

import com.timberliu.im.protocol.AbstractPacket;
import com.timberliu.im.session.Session;
import lombok.Data;

import java.util.List;

import static com.timberliu.im.protocol.command.Command.LIST_GROUP_MEMBERS_RESPONSE;

/**
 * Created by liujie on 2021/6/23
 */
@Data
public class ListGroupMembersResponsePacket extends AbstractPacket {

    private boolean success;

    private String groupId;

    private List<Session> sessionList;

    @Override
    public byte getCommand() {
        return LIST_GROUP_MEMBERS_RESPONSE;
    }
}
