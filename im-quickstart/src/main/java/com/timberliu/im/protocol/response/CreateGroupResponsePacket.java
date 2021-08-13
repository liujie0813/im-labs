package com.timberliu.im.protocol.response;

import com.timberliu.im.protocol.AbstractPacket;
import lombok.Data;

import java.util.List;

import static com.timberliu.im.protocol.command.Command.CREATE_GROUP_RESPONSE;

/**
 * Created by liujie on 2021/6/23
 */
@Data
public class CreateGroupResponsePacket extends AbstractPacket {

    private boolean success;

    private String groupId;

    private List<String> userNameList;

    @Override
    public byte getCommand() {
        return CREATE_GROUP_RESPONSE;
    }
}
