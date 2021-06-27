package com.timberliu.im.protocol.request;

import com.timberliu.im.protocol.AbstractPacket;

import static com.timberliu.im.protocol.command.Command.HEARTBEAT_REQUEST;

/**
 * Created by liujie on 2021/6/25
 */

public class HeartBeatRequestPacket extends AbstractPacket {

    @Override
    public byte getCommand() {
        return HEARTBEAT_REQUEST;
    }
}
