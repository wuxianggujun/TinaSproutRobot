package com.wuxianggujun.robotcore.listener;

import com.wuxianggujun.robotcore.listener.message.MessageEvent;

public interface MessageListener<T extends MessageEvent> extends EventListener {
    void handler(T message);
}
