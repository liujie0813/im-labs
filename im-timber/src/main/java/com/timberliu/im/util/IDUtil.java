package com.timberliu.im.util;

import java.util.UUID;

/**
 * Created by liujie on 2021/6/23
 */

public class IDUtil {

    public static String randomId() {
        return UUID.randomUUID().toString().split("-")[0];
    }
}
