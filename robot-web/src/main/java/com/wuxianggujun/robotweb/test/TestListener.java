package com.wuxianggujun.robotweb.test;

import com.wuxianggujun.robotcore.annotation.Bot;
import com.wuxianggujun.robotcore.annotation.MessageEvent;
import com.wuxianggujun.robotcore.enums.MessageEventType;
import com.wuxianggujun.robotcore.listener.impl.GroupMessageListener;
import com.wuxianggujun.robotcore.listener.message.GroupMessageEvent;

@MessageEvent(MessageEventType.GROUP)
public class TestListener implements GroupMessageListener {

    @Override
    public void handler(GroupMessageEvent message) {

    }
}
