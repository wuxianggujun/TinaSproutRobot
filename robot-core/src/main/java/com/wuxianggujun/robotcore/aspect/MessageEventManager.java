package com.wuxianggujun.robotcore.aspect;

import com.wuxianggujun.robotcore.annotation.MessageEvent;
import com.wuxianggujun.robotcore.listener.MessageEventContext;
import com.wuxianggujun.robotcore.listener.MessageListener;
import org.reflections.Reflections;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.util.Set;

/**
 * 反射获取
 */
@Configuration
public class MessageEventManager {
    //扫描包
    @PostConstruct
    private void scan() {
        doMessageEvent();
    }

    public void doMessageEvent() {
        //扫描包名
        Reflections reflections = new Reflections("com.wuxianggujun.robotweb");
        //获取带注解的类
        Set<Class<?>> typesAnnotatedWith = reflections.getTypesAnnotatedWith(MessageEvent.class);
        for (Class<?> clazz : typesAnnotatedWith) {
            String messageType = clazz.getAnnotation(MessageEvent.class).value();
            try {
                if (messageType.equals("group") || messageType.equals("private")) {
                    MessageEventContext.getInstance().addEventListener(messageType,(MessageListener) clazz.getDeclaredConstructor().newInstance());
                } else {
                    throw new IllegalStateException("不要用不存在的字段");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }
}
