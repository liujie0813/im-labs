package com.timberliu.im.client.netty.exer3.serialize;

import com.timberliu.im.client.netty.exer3.serialize.impl.JSONSerializer;

/**
 * Created by liujie on 2021/6/22
 */

public interface Serializer {

    byte JSON_SERIALIZER = 1;

    Serializer DEFAULT = new JSONSerializer();

    /**
     * 序列化算法
     */
    byte getSerializerAlgorithm();

    /**
     * java 对象转换成二进制
     */
    byte[] serialize(Object object);

    /**
     * 二进制转换成 java 对象
     */
    <T> T deserialize(byte[] bytes, Class<T> clazz);
}
