package com.wuxianggujun.robotweb.test;

import com.wuxianggujun.robotcore.annotation.MessageEvent;
import com.wuxianggujun.robotcore.enums.MessageEventType;
import com.wuxianggujun.robotcore.listener.impl.GroupMessageListener;
import com.wuxianggujun.robotcore.listener.message.GroupMessageEvent;

@MessageEvent(MessageEventType.GROUP)
public class TestListener implements GroupMessageListener {
    public TestListener(){

    }
    public TestListener(String string){

    }

    @Override
    public void handler(GroupMessageEvent message) {
        System.out.println("群主的消息" + message.getMessage());
    }
}
