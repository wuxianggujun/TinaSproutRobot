package com.wuxianggujun.robotweb.test;

import com.wuxianggujun.robotcore.annotation.Bot;
import com.wuxianggujun.robotcore.listener.impl.GroupMessageListener;
import com.wuxianggujun.robotcore.listener.message.GroupMessageEvent;

@Bot
public class TestGroupMessageListener extends com.wuxianggujun.robotcore.core.bot.Bot implements GroupMessageListener {
    @Override
    public void handler(GroupMessageEvent message) {
        System.out.println("我喜欢你" + message.getMessage());

    }
}
