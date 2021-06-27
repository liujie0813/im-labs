package com.timberliu.im.client.netty.api;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;

/**
 * Created by liujie on 2021/6/21
 */

public class ByteBufTest {

    public static void main(String[] args) {
        // 堆外内存
        ByteBuf directBuffer = ByteBufAllocator.DEFAULT.directBuffer(9, 100);
        // 堆内存
        ByteBuf byteBuf = ByteBufAllocator.DEFAULT.buffer(9, 100);

        // 容量，包括丢弃字节、可读字节、可写字节
        int capacity = byteBuf.capacity();

        // 最大容量，写数据时如果容量不足就进行扩容，直到扩容到 maxCapacity，如果超过则报错
        int maxCapacity = byteBuf.maxCapacity();

        // 可读的字节数，等于 writeIndex - readIndex
        int readableBytes = byteBuf.readableBytes();
        // 是否可读，如果 readIndex 等于 writeIndex，则不可读
        boolean readable = byteBuf.isReadable();

        // 可写的字节数，等于 capacity - writeIndex
        int writableBytes = byteBuf.writableBytes();
        // 是否可写，如果 capacity 等于 writeIndex，则返回不可写，但并不代表不能写了，而是会自动扩容
        boolean writable = byteBuf.isWritable();
        // 最大的可写的字节数
        int maxWritableBytes = byteBuf.maxWritableBytes();



        // 返回当前的读指针
        int readerIndex = byteBuf.readerIndex();
        // 设置
        byteBuf.readerIndex(readerIndex - 3);

        // 返回当前的写指针
        int writerIndex = byteBuf.writerIndex();
        byteBuf.writerIndex(writerIndex - 3);

        // 标记
        byteBuf.markReaderIndex();
        // 恢复
        byteBuf.resetReaderIndex();



        // 把 "hello" 写到 ByteBuf 中
        byteBuf.writeBytes("hello".getBytes());
        byte[] readBytes = new byte[byteBuf.readableBytes()];
        // 把 ByteBuf 里的数据读取到 dst
        byteBuf.readBytes(readBytes);

        // 写数据，不会改变读写指针
        byteBuf.setBytes(0, "hello".getBytes(), 0, 5);
        byte[] setBytes = new byte[byteBuf.readableBytes()];
        byteBuf.getBytes(1, setBytes, 0, 5);



        // 堆外内存必须手动释放
        // 引用计数减 1，如果计数为 0，则释放
        boolean release = byteBuf.release();
        // 引用计数加 1
        ByteBuf retain = byteBuf.retain();

        // 截取
        // 从 readerIndex 到 writerIndex，其最大容量 maxCapacity 为原始 ByteBuf 的 readableBytes()
        ByteBuf slice = byteBuf.slice();
        // 重复
        // 把整个 ByteBuf 都截取出来，包括所有的数据、指针信息
        ByteBuf duplicate = byteBuf.duplicate();
        // 复制
        // 从原始的 ByteBuf 拷贝出所有信息，包括读写指针及底层数据
        ByteBuf copy = byteBuf.copy();

        // 截取时增加内存引用计数
        ByteBuf retainedSlice = byteBuf.retainedSlice();
        ByteBuf retainedDuplicate = byteBuf.retainedDuplicate();


    }
}
