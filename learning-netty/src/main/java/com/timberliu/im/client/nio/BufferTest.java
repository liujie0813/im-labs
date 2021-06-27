package com.timberliu.im.client.nio;

import java.nio.IntBuffer;

/**
 * Created by liujie on 2021/6/20
 */

public class BufferTest {

    public static void main(String[] args) {
        // 创建一个容量为 5、存放 int 类型的 Buffer
        IntBuffer intBuffer = IntBuffer.allocate(5);

        intBuffer.put(1);
        intBuffer.put(new int[]{1, 2, 3, 4, 5}, 1, 4);

        // 切换读写模式
        intBuffer.flip();

        while (intBuffer.hasRemaining()) {
            System.out.println(intBuffer.get());
        }
    }

}
