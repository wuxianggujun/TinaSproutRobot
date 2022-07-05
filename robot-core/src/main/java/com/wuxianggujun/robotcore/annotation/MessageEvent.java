package com.wuxianggujun.robotcore.annotation;

import com.wuxianggujun.robotcore.enums.MessageEventType;

import java.lang.annotation.*;

/**
 * 消息注解
 */
@Target({ElementType.TYPE})
//@Retention(RetentionPolicy.RUNTIME)
@Retention(RetentionPolicy.SOURCE)
@Documented
public @interface MessageEvent {
    MessageEventType value();
}
