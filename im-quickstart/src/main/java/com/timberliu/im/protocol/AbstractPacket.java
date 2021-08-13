package com.timberliu.im.protocol;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

/**
 * Created by liujie on 2021/6/22
 */
@Data
public abstract class AbstractPacket {

    /**
     * 协议版本
     */
    @JSONField(deserialize = false, serialize = false)
    private byte version = 1;

    /**
     * 指令
     */
    @JSONField(serialize = false)
    public abstract byte getCommand();

}
