package com.wuxianggujun.robotcore.reflections;

import annotation.MessageEvent;
import annotation.enums.MessageEventType;
import com.wuxianggujun.robotcore.listener.MessageEventContext;
import com.wuxianggujun.robotcore.listener.MessageListener;
import com.wuxianggujun.robotcore.listener.impl.GroupMessageListener;
import com.wuxianggujun.robotcore.listener.impl.PrivateMessageListener;
import org.reflections.Reflections;
import org.reflections.scanners.Scanners;
import org.reflections.util.ConfigurationBuilder;
import org.reflections.util.FilterBuilder;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.lang.reflect.Modifier;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 反射获取
 */
@Configuration
public class MessageEventManager {
//    private final Map<String, Object> map = new ConcurrentHashMap<>();

    //扫描包
    @PostConstruct
    private void scan() {
        doMessageEvent();
    }

    public void doMessageEvent() {
        Reflections reflections1 = new Reflections(new ConfigurationBuilder()
                .forPackages("com.wuxianggujun.robotweb")
                .setScanners(Scanners.values())
                .filterInputsBy(new FilterBuilder()
                        .includePackage("com.wuxianggujun.robotweb")
                        .excludePackage("com.wuxianggujun.robotcore")
                        .excludePackage("com.wuxianggujun.robotbase")));
        Set<Class<? extends MessageListener>> classSet = reflections1.getSubTypesOf(MessageListener.class);
        //可以用来判断是不是某个接口的实现类然后执行添加操作
        List<Class<? extends MessageListener>> filters = classSet.stream()
                .filter(aClass -> !Modifier.isAbstract(aClass.getModifiers())).collect(Collectors.toList());
        for (Class<? extends MessageListener> clazz : filters) {
            //获取注解
            MessageEvent messageEvent = clazz.getAnnotation(MessageEvent.class);

            //判断为不为null
            if (messageEvent != null) {
                MessageEventType messageEventType = messageEvent.value();
                /**
                 * isAssignableFrom
                 * 也就是判断当前的Class对象所表示的类，是不是参数中传递的Class对象所表示的类的父类，超接口，或者是相同的类型。是则返回true，否则返回false。
                 */
                if (GroupMessageListener.class.isAssignableFrom(clazz) && messageEventType.getMessageType().equals("group")) {
                    try {
                        MessageEventContext.getInstance().addEventListener(messageEventType.getMessageType(), clazz.getDeclaredConstructor().newInstance());
                    } catch (Exception e) {
                        throw new RuntimeException("添加群消息监听器错误:" + e);
                    }
                } else if (PrivateMessageListener.class.isAssignableFrom(clazz) && messageEventType.getMessageType().equals("private")) {
                    try {
                        MessageEventContext.getInstance().addEventListener(messageEventType.getMessageType(), clazz.getDeclaredConstructor().newInstance());
                    } catch (Exception e) {
                        throw new RuntimeException("添加私信消息监听器错误:" + e);
                    }
                }
            }
        }

    }
}
