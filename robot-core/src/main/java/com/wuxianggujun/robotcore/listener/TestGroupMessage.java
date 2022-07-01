package com.wuxianggujun.robotcore.listener;

import com.wuxianggujun.robotcore.listener.impl.GroupMessageListener;
import com.wuxianggujun.robotcore.listener.message.GroupMessage;

public class TestGroupMessage implements GroupMessageListener {
    @Override
    public void handler(GroupMessage message) {
        System.out.println("我是群消息");
    }
}
