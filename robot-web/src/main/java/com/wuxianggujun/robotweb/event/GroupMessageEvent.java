package com.wuxianggujun.robotweb.event;

import com.wuxianggujun.annotation.MessageEvent;
import com.wuxianggujun.annotation.enums.MessageEventType;
import com.wuxianggujun.robotbase.cache.ObjectCache;
import com.wuxianggujun.robotcore.annotation.BotAnnotation;
import com.wuxianggujun.robotcore.listener.impl.GroupMessageListener;
import com.wuxianggujun.robotweb.test.Test;

@MessageEvent(MessageEventType.GROUP)
public class GroupMessageEvent implements GroupMessageListener {

    @BotAnnotation("我是")
    private String test = "你好";

    @Override
    public void handler(com.wuxianggujun.robotcore.listener.message.GroupMessageEvent message) {
        Test test = (Test) ObjectCache.getInstance().getCache(Test.class);
        System.out.println(test.getBot().getName());
        System.out.println(test.toString());
        System.out.println(ObjectCache.getInstance().getCache().size());
    }
}
