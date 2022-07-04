package com.wuxianggujun.robotcore.annotation;

import java.lang.annotation.*;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface BotAnnotation {
    String value() default "";
}
