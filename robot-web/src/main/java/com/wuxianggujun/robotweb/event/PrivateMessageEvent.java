package com.wuxianggujun.robotweb.event;

import com.wuxianggujun.robotcore.listener.impl.PrivateMessageListener;

public class PrivateMessageEvent implements PrivateMessageListener {
    @Override
    public void handler(com.wuxianggujun.robotcore.listener.message.PrivateMessageEvent message) {
        System.out.println("这是私聊信息" + message.getMessage());
    }
}
