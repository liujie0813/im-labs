package com.timberliu.im.protocol.response;

import com.timberliu.im.protocol.AbstractPacket;

import static com.timberliu.im.protocol.command.Command.HEARTBEAT_REQUEST;
import static com.timberliu.im.protocol.command.Command.HEARTBEAT_RESPONSE;

/**
 * Created by liujie on 2021/6/25
 */

public class HeartBeatResponsePacket extends AbstractPacket {

    @Override
    public byte getCommand() {
        return HEARTBEAT_RESPONSE;
    }
}
