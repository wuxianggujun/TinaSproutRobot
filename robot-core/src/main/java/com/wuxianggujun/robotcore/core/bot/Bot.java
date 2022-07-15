package com.wuxianggujun.robotcore.core.bot;

import com.wuxianggujun.robotcore.listener.message.MessageEvent;
import org.springframework.stereotype.Component;

@Component
public class Bot {
    private long selfId;

    private MessageEvent messageEvent;


    public long getSelfId() {
        return selfId;
    }

    public void setSelfId(long selfId) {
        this.selfId = selfId;
    }

    public MessageEvent getMessageEvent() {
        return messageEvent;
    }

    public void setMessageEvent(MessageEvent messageEvent) {
        this.messageEvent = messageEvent;
    }
    
    
    
    
}