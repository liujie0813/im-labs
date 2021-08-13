package com.timberliu.im.protocol.request;

import com.timberliu.im.protocol.AbstractPacket;
import lombok.Data;

import java.util.List;

import static com.timberliu.im.protocol.command.Command.CREATE_GROUP_REQUEST;

/**
 * Created by liujie on 2021/6/23
 */
@Data
public class CreateGroupRequestPacket extends AbstractPacket {

    private List<String> userIdList;

    @Override
    public byte getCommand() {
        return CREATE_GROUP_REQUEST;
    }
}
