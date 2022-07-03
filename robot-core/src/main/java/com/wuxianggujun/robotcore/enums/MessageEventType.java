package com.wuxianggujun.robotcore.enums;

/**
 * 定义消息枚举给注解用的
 */
public enum MessageEventType {

    //群消息
    GROUP("group"),
    //私聊信息
    PRIVATE("private"),
    //通知
    NOTICE("notices");
    private final String messageType;

    MessageEventType(String messageType) {
        this.messageType = messageType;
    }

    public String getMessageType() {
        return messageType;
    }


}
