package com.wuxianggujun.robotcore.listener.impl;

import com.wuxianggujun.robotcore.listener.MessageListener;
import com.wuxianggujun.robotcore.listener.message.PrivateMessageEvent;

public interface PrivateMessageListener extends MessageListener<PrivateMessageEvent> {

    @Override
    void handler(PrivateMessageEvent message);
}
