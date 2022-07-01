package com.wuxianggujun.robotcore.listener;


import com.wuxianggujun.robotcore.listener.impl.GroupMessageListener;
import com.wuxianggujun.robotcore.listener.impl.PrivateMessageListener;
import com.wuxianggujun.robotcore.listener.message.GroupMessage;
import com.wuxianggujun.robotcore.listener.message.MessageEvent;
import com.wuxianggujun.robotcore.listener.message.PrivateMessage;

import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class MessageEventContext {
    //监听者合集
    private final Map<String, LinkedHashSet<MessageListener>> eventHandlers = new ConcurrentHashMap<>();

    private static volatile MessageEventContext instance = null;

    private MessageEventContext() {

    }

    public static MessageEventContext getInstance() {
        if (instance == null) {
            synchronized (MessageEventContext.class) {
                if (instance == null) {
                    instance = new MessageEventContext();
                }
            }
        }
        return instance;
    }


    public void addEventListener(MessageListener messageListener) {
        if (messageListener instanceof GroupMessageListener) {
            addEventListener("group", messageListener);
        } else if (messageListener instanceof PrivateMessageListener) {
            addEventListener("private", messageListener);
        }
    }

    public void addEventListener(String messageType, MessageListener messageListener) {
        //先判断包不包含group分组
        LinkedHashSet<MessageListener> listeners = eventHandlers.get(messageType);
        if (listeners == null) {
            listeners = new LinkedHashSet<>();
        }
        listeners.add(messageListener);
        eventHandlers.put(messageType, listeners);
    }

    public void handler(MessageEvent messageEvent) {
        if (messageEvent.getMessageType() != null) {
            String messageType = null;
            if (messageEvent.getMessageType().equals("group")) {
                GroupMessage groupMessage = (GroupMessage) messageEvent;
                messageType = groupMessage.getMessageType();
            } else if (messageEvent.getMessageType().equals("private")) {
                PrivateMessage privateMessage = (PrivateMessage) messageEvent;
                messageType = privateMessage.getMessageType();
            }
            LinkedHashSet<MessageListener> listeners = eventHandlers.get(messageType);
            Iterator iterator = listeners.iterator();
            while (iterator.hasNext()) {
                System.out.println("LinkedHashSet:" + listeners.size());
                MessageListener listener = (MessageListener) iterator.next();
                listener.handler(messageEvent);
            }
        }

    }

}
