package com.wuxianggujun.robotbase.annotation;

import com.wuxianggujun.robotbase.enums.ScopeConst;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface AutoWired {
    /**
     * 指定依赖名称
     *
     * @return
     */
    String value() default "";

    /**
     * 指定创建类的模式 单利还是多例
     *
     * @return
     */
    ScopeConst scope() default ScopeConst.SINGLETON;
}
