package com.wuxianggujun.robotweb.event;

import annotation.MessageEvent;
import annotation.enums.MessageEventType;
import com.wuxianggujun.robotcore.annotation.BotAnnotation;
import com.wuxianggujun.robotcore.listener.impl.GroupMessageListener;
import com.wuxianggujun.robotcore.message.SendGroupForwardMessage;

@MessageEvent(MessageEventType.GROUP)
public class GroupMessageEvent implements GroupMessageListener {
    
    @Override
    public void handler(com.wuxianggujun.robotcore.listener.message.GroupMessageEvent message) {
        SendGroupForwardMessage sendGroupForwardMessage = new SendGroupForwardMessage(message.getMessage());
        System.out.println("群消息:" + message.getMessage());
    }
}