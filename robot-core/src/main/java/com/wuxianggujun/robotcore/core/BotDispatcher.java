package com.wuxianggujun.robotcore.core;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wuxianggujun.robotcore.listener.MessageEventContext;
import com.wuxianggujun.robotcore.listener.TestGroupMessage;
import com.wuxianggujun.robotcore.listener.message.GroupMessage;
import com.wuxianggujun.robotcore.listener.message.MessageEvent;
import com.wuxianggujun.robotcore.listener.message.PrivateMessage;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 机器人调度程序
 *
 * @author 无相孤君
 * @date 2022/06/28
 */
public class BotDispatcher {

    //创建线程池
    ExecutorService threadPool = Executors.newFixedThreadPool(5);

    //序列化JSON转化为对象
    private ObjectMapper objectMapper;

    private MessageEventContext messageEventContext = null;

    private static volatile BotDispatcher instance = null;

    private BotDispatcher() {

    }

    public static BotDispatcher getInstance() {
        if (instance == null) {
            synchronized (BotDispatcher.class) {
                if (instance == null) {
                    instance = new BotDispatcher();
                }
            }
        }
        return instance;
    }

    /**
     * 初始化方法
     */
    private void init() {
        objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules();
        //反序列化的时候如果多了其他属性,不抛出异常
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        messageEventContext = MessageEventContext.getInstance();

    }


    public void handle(String text) {
        init();
        threadPool.submit(() -> {
            try {
                JsonNode jsonNode = objectMapper.readTree(text);
                JsonNode postType = jsonNode.get("post_type");
                System.out.println(jsonNode.asText());
                //先创建机器人实例对象
                JsonNode self_id = jsonNode.get("self_id");

                //解析JSON判断是不是message消息还是心跳包
                if (postType.asText().equals("message")) {

                    JsonNode message_type = jsonNode.get("message_type");
                    MessageEvent messageEvent = null;

                    switch (message_type.asText()) {
                        //如果是群聊信息
                        case "group":
                            GroupMessage groupMessage = objectMapper.readValue(text, GroupMessage.class);

                            messageEvent = groupMessage;
                            break;
                        case "private":
                            PrivateMessage privateMessage = objectMapper.readValue(text, PrivateMessage.class);
                            break;
                        default:
                            System.out.println("MessageType:" + message_type.asText());
                    }
                    messageEventContext.addEventListener(new TestGroupMessage());
                    messageEventContext.handler(messageEvent);
                } else if (postType.asText().equals("meta_event")) {
                    //元事件
                    //System.out.println(postType);
                } else if (postType.asText().equals("request")) {
                    //请求
                } else if (postType.asText().equals("notice")) {
                    //通知
                }
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        });

    }


}
