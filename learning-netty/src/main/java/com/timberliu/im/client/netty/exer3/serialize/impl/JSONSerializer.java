package com.timberliu.im.client.netty.exer3.serialize.impl;

import com.alibaba.fastjson.JSON;
import com.timberliu.im.client.netty.exer3.serialize.Serializer;
import com.timberliu.im.client.netty.exer3.serialize.SerializerAlgorithm;

/**
 * Created by liujie on 2021/6/22
 */

public class JSONSerializer implements Serializer {

    @Override
    public byte getSerializerAlgorithm() {
        return SerializerAlgorithm.JSON;
    }

    @Override
    public byte[] serialize(Object object) {
        return JSON.toJSONBytes(object);
    }

    @Override
    public <T> T deserialize(byte[] bytes, Class<T> clazz) {
        return JSON.parseObject(bytes, clazz);
    }
}
