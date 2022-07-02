package com.wuxianggujun.robotcore.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

@Target({ElementType.FIELD})
public @interface BotTest {
    String value() default "";
}
