package com.wuxianggujun.robotcore.listener.impl;

import com.wuxianggujun.robotcore.listener.MessageListener;
import com.wuxianggujun.robotcore.listener.message.PrivateMessage;

public interface PrivateMessageListener extends MessageListener<PrivateMessage> {

    @Override
    void handler(PrivateMessage message);
}
