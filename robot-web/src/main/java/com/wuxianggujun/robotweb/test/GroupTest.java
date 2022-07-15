package com.wuxianggujun.robotweb.test;

import annotation.MessageEvent;
import annotation.enums.MessageEventType;
import com.wuxianggujun.robotbase.annotation.AutoWired;
import com.wuxianggujun.robotcore.listener.impl.GroupMessageListener;
import com.wuxianggujun.robotcore.listener.message.GroupMessageEvent;

@MessageEvent(MessageEventType.GROUP)
public class GroupTest implements GroupMessageListener {
    @AutoWired
    Test test;

    @Override
    public void handler(GroupMessageEvent message) {
        test.getBot();
    }
}