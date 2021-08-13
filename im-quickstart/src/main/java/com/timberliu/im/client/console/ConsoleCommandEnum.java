package com.timberliu.im.client.console;

/**
 * Created by liujie on 2021/6/24
 */

public enum ConsoleCommandEnum {

    SEND_TO_USER(1, "sendToUser"),
    LOGOUT(2, "logout"),
    CREATE_GROUP(3, "createGroup"),
    JOIN_GROUP(4, "joinGroup"),
    QUIT_GROUP(5, "quitGroup"),
    LIST_GROUP_MEMBERS(6, "listGroupMembers"),
    SEND_TO_GROUP(7, "sendToGroup");

    private Integer code;
    private String content;

    ConsoleCommandEnum(Integer code, String content) {
        this.code = code;
        this.content = content;
    }

    public Integer getCode() {
        return code;
    }

    public String getContent() {
        return content;
    }
}
