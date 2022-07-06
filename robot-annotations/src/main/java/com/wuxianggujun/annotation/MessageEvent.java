package com.wuxianggujun.annotation;

import com.wuxianggujun.annotation.enums.MessageEventType;

import java.lang.annotation.*;

/**
 * 消息注解
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.SOURCE)
@Documented
public @interface MessageEvent {
    MessageEventType value();
}
