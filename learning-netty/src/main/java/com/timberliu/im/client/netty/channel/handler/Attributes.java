package com.timberliu.im.client.netty.channel.handler;

import io.netty.util.AttributeKey;

/**
 * Created by liujie on 2021/6/23
 */

public interface Attributes {

    AttributeKey<Integer> ATTRIBUTE_KEY = AttributeKey.newInstance("count");
}
