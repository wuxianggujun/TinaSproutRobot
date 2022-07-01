package com.wuxianggujun.robotcore.listener.impl;

import com.wuxianggujun.robotcore.listener.MessageListener;
import com.wuxianggujun.robotcore.listener.message.GroupMessage;

public interface GroupMessageListener extends MessageListener<GroupMessage>{

    @Override
    void handler(GroupMessage message);
}
