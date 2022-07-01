package com.wuxianggujun.robotcore.annotation;

import java.lang.annotation.*;

/**
 * 消息注解
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface MessageEvent {

    String value() default "";
}
