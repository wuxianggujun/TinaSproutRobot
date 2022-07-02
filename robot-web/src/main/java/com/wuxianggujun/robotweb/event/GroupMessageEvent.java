package com.wuxianggujun.robotweb.event;

import com.wuxianggujun.robotcore.annotation.BotTest;
import com.wuxianggujun.robotcore.annotation.MessageEvent;
import com.wuxianggujun.robotcore.listener.impl.GroupMessageListener;
import com.wuxianggujun.robotcore.listener.message.GroupMessage;

@MessageEvent(value = "group")
public class GroupMessageEvent implements GroupMessageListener {

    @BotTest("我是")
    private String test;

    @Override
    public void handler(GroupMessage message) {
        //bot.setName("wuxianggujun");
        System.out.println(message.getMessage());
    }
}
