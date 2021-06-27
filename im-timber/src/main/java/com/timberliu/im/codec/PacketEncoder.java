package com.timberliu.im.codec;

import com.timberliu.im.protocol.AbstractPacket;
import com.timberliu.im.protocol.PacketCodec;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * Created by liujie on 2021/6/22
 */

public class PacketEncoder extends MessageToByteEncoder<AbstractPacket> {

    @Override
    protected void encode(ChannelHandlerContext ctx, AbstractPacket abstractPacket, ByteBuf out) throws Exception {
        PacketCodec.INSTANCE.encode(out, abstractPacket);
    }
}
