package com.wuxianggujun.robotweb.test;

import annotation.MessageEvent;
import annotation.enums.MessageEventType;
import com.wuxianggujun.robotcore.annotation.BotAnnotation;
import com.wuxianggujun.robotcore.core.bot.Bot;

@MessageEvent(MessageEventType.NOTICE)
public class Test {
    @BotAnnotation
    private int i = 1000;

    @BotAnnotation
    private Bot bot;

    public int getI() {
        return i;
    }

    public Bot getBot() {
        return bot;
    }
}
