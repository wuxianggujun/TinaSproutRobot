package com.wuxianggujun.robotcore.listener.impl;

import com.wuxianggujun.robotcore.listener.MessageListener;
import com.wuxianggujun.robotcore.listener.message.GroupMessageEvent;

public interface GroupMessageListener extends MessageListener<GroupMessageEvent>{

    @Override
    void handler(GroupMessageEvent message);
}
