package com.wuxianggujun.robotweb.event;

import annotation.MessageEvent;
import annotation.enums.MessageEventType;
import cn.hutool.extra.spring.SpringUtil;
import com.wuxianggujun.robotcore.annotation.BotAnnotation;
import com.wuxianggujun.robotcore.core.bot.BotClient;
import com.wuxianggujun.robotcore.listener.impl.GroupMessageListener;
import com.wuxianggujun.robotcore.message.SendGroupForwardMessage;
import com.wuxianggujun.robotcore.message.SendGroupMessage;
import com.wuxianggujun.robotcore.message.SendMessage;

@MessageEvent(MessageEventType.GROUP)
public class GroupMessageEvent implements GroupMessageListener {
    
    @Override
    public void handler(com.wuxianggujun.robotcore.listener.message.GroupMessageEvent message) {
        SpringUtil.getBean(BotClient.class).invokeApi(new SendGroupMessage(537756994l,"狗哥真棒！"));
        System.out.println("群消息:" + message.getMessage());
    }
}