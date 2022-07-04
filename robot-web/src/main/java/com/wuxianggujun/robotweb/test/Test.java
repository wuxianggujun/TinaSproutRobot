package com.wuxianggujun.robotweb.test;

import com.wuxianggujun.robotcore.annotation.BotAnnotation;
import com.wuxianggujun.robotcore.core.bot.Bot;

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
