package com.wuxianggujun.robotweb.test;

import annotation.MessageEvent;
import annotation.enums.MessageEventType;
import com.wuxianggujun.robotbase.annotation.AutoWired;
import com.wuxianggujun.robotcore.annotation.BotAnnotation;
import com.wuxianggujun.robotcore.core.bot.Bot;

@MessageEvent(MessageEventType.NOTICE)
public class Test {
    @BotAnnotation
    private int i = 1000;

    @AutoWired
    private Bot bot;

    public int getI() {
        return i;
    }

    public Bot getBot() {
        return bot;
    }
}