package com.wuxianggujun.robotbase.annotation;

import java.lang.annotation.Annotation;

public @interface Pointcut {
    String value() default "";
}
