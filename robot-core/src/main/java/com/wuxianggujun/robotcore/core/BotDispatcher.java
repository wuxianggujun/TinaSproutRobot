package com.wuxianggujun.robotcore.core;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wuxianggujun.robotcore.annotation.TestAnnotation;
import com.wuxianggujun.robotcore.reflections.EventTypeRepository;
import com.wuxianggujun.robotcore.listener.MessageEventContext;
import com.wuxianggujun.robotcore.listener.message.GroupMessageEvent;
import com.wuxianggujun.robotcore.listener.message.MessageEvent;
import com.wuxianggujun.robotcore.listener.message.PrivateMessageEvent;
import org.reflections.Reflections;
import org.reflections.scanners.Scanners;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;

import java.lang.reflect.Field;
import java.util.Set;
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


                ConfigurationBuilder configurationBuilder = new ConfigurationBuilder()
                        .addUrls(ClasspathHelper.forPackage("com.wuxianggujun"))
                        .addScanners(Scanners.FieldsAnnotated);
                //扫描包名
                Reflections reflections = new Reflections(configurationBuilder);
                EventTypeRepository typeRepository = new EventTypeRepository(reflections);
                Set<Field> cao = reflections.getFieldsAnnotatedWith(TestAnnotation.class);

                for (Field field : typeRepository.getTypeProperties()) {
                    TestAnnotation testAnnotation = field.getAnnotation(TestAnnotation.class);
                    System.out.println("自定义注解：" + testAnnotation.value());
                }

                //解析JSON判断是不是message消息还是心跳包
                if (postType.asText().equals("message")) {

                    JsonNode message_type = jsonNode.get("message_type");
                    MessageEvent messageEvent = null;


                    switch (message_type.asText()) {
                        //如果是群聊信息
                        case "group":
                            GroupMessageEvent groupMessage = objectMapper.readValue(text, GroupMessageEvent.class);

                            messageEvent = groupMessage;
                            break;
                        case "private":
                            PrivateMessageEvent privateMessage = objectMapper.readValue(text, PrivateMessageEvent.class);
                            messageEvent = privateMessage;
                            break;
                        default:
                            System.out.println("MessageType:" + message_type.asText());
                    }
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
