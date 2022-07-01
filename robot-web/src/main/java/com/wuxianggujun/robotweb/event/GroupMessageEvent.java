package com.wuxianggujun.robotweb.event;

import com.wuxianggujun.robotcore.annotation.MessageEvent;
import com.wuxianggujun.robotcore.listener.impl.GroupMessageListener;
import com.wuxianggujun.robotcore.listener.message.GroupMessage;

@MessageEvent(value = "group")
public class GroupMessageEvent implements GroupMessageListener {
    @Override
    public void handler(GroupMessage message) {
        System.out.println(message.getMessage());
    }
}
