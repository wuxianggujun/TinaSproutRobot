package com.wuxianggujun.robotweb.event;

import com.wuxianggujun.robotcore.annotation.MessageEvent;
import com.wuxianggujun.robotcore.core.bot.Bot;
import com.wuxianggujun.robotcore.listener.impl.GroupMessageListener;
import com.wuxianggujun.robotcore.listener.message.GroupMessage;

@MessageEvent(value = "group")
public class GroupMessageEvent implements GroupMessageListener {

    private Bot bot;

    private String test;

    @Override
    public void handler(GroupMessage message) {
        System.out.println(message.getMessage());
    }
}
