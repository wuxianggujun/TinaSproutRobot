package com.wuxianggujun.robotweb.test;

import com.wuxianggujun.robotcore.annotation.MessageEvent;
import com.wuxianggujun.robotcore.enums.MessageEventType;
import com.wuxianggujun.robotcore.listener.impl.GroupMessageListener;
import com.wuxianggujun.robotcore.listener.message.GroupMessageEvent;

@MessageEvent(MessageEventType.GROUP)
public class TestGroupMessageListener extends com.wuxianggujun.robotcore.core.bot.Bot implements GroupMessageListener {
    @Override
    public void handler(GroupMessageEvent message) {
        System.out.println("我喜欢你" + message.getMessage());

    }
}
