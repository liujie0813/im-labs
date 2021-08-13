package com.timberliu.im.attribute;

import com.timberliu.im.session.Session;
import io.netty.util.AttributeKey;

/**
 * Created by liujie on 2021/6/22
 */

public interface Attributes {

    AttributeKey<Boolean> LOGIN = AttributeKey.newInstance("login");

    AttributeKey<Session> SESSION = AttributeKey.newInstance("session");
}
