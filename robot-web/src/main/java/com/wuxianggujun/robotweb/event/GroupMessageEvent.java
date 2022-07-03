package com.wuxianggujun.robotweb.event;

import com.wuxianggujun.robotcore.annotation.Bot;
import com.wuxianggujun.robotcore.annotation.MessageEvent;
import com.wuxianggujun.robotcore.enums.MessageEventType;
import com.wuxianggujun.robotcore.listener.impl.GroupMessageListener;

@MessageEvent(MessageEventType.GROUP)
public class GroupMessageEvent implements GroupMessageListener {

    @Bot("我是")
    private String test = "你好";

    @Override
    public void handler(com.wuxianggujun.robotcore.listener.message.GroupMessageEvent message) {
        //bot.setName("wuxianggujun");
        System.out.println(message.getMessage());
    }
}
