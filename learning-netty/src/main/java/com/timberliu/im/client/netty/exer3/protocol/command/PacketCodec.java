package com.timberliu.im.client.netty.exer3.protocol.command;

import com.timberliu.im.client.netty.exer3.serialize.Serializer;
import com.timberliu.im.client.netty.exer3.serialize.impl.JSONSerializer;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;

import java.util.HashMap;
import java.util.Map;

import static com.timberliu.im.client.netty.exer3.protocol.command.Command.LOGIN_REQUEST;

/**
 * Created by liujie on 2021/6/22
 */

public class PacketCodec {

    private static final int MAGIC_NUMBER = 0x19982021;
    private static final Map<Byte, Class<? extends AbstractPacket>> packetTypeMap;
    private static final Map<Byte, Serializer> serializerMap;

    static {
        packetTypeMap = new HashMap<>();
        packetTypeMap.put(LOGIN_REQUEST, LoginRequestPacket.class);

        serializerMap = new HashMap<>();
        JSONSerializer jsonSerializer = new JSONSerializer();
        serializerMap.put(jsonSerializer.getSerializerAlgorithm(), jsonSerializer);
    }

    public ByteBuf encode(AbstractPacket packet) {
        // ioBuffer 方法返回适配 io 读写相关的内存，尽可能创建一个直接内存，不受堆管理，写到 IO 缓冲区效率更高
        ByteBuf byteBuf = ByteBufAllocator.DEFAULT.ioBuffer();
        byte[] bytes = Serializer.DEFAULT.serialize(packet);

        byteBuf.writeInt(MAGIC_NUMBER);
        byteBuf.writeByte(packet.getVersion());
        byteBuf.writeByte(Serializer.DEFAULT.getSerializerAlgorithm());
        byteBuf.writeByte(packet.getCommand());
        byteBuf.writeInt(bytes.length);
        byteBuf.writeBytes(bytes);

        return byteBuf;
    }

    public AbstractPacket decode(ByteBuf byteBuf) {
        // skip magic number
        byteBuf.skipBytes(4);

        // skip version
        byteBuf.skipBytes(1);

        byte serializerAlgorithm = byteBuf.readByte();
        byte command = byteBuf.readByte();
        int dataLen = byteBuf.readInt();

        byte[] bytes = new byte[dataLen];
        byteBuf.readBytes(bytes);

        Class<? extends AbstractPacket> requestType = getRequestType(command);
        Serializer serializer = getSerializer(serializerAlgorithm);

        if (requestType != null && serializer != null) {
            return serializer.deserialize(bytes, requestType);
        }
        return null;
    }

    private Serializer getSerializer(byte serializerAlgorithm) {
        return serializerMap.get(serializerAlgorithm);
    }

    private Class<? extends AbstractPacket> getRequestType(byte command) {
        return packetTypeMap.get(command);
    }

}
