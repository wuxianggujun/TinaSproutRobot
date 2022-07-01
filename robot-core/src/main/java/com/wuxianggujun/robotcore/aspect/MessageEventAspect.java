package com.wuxianggujun.robotcore.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class MessageEventAspect {
    public final Logger logger = LoggerFactory.getLogger(this.getClass().getName());

    @Pointcut("@annotation(com.wuxianggujun.robotcore.annotation.MessageEvent)")
    public void annotationPointcut() {

    }

    @Before("annotationPointcut()")
    public void beforePointcut() {
        System.out.println("执行了切面……");
        //此处进入到方法之前，可以实现一些业务逻辑
    }

    @Around("annotationPointcut()")
    public Object doAround(ProceedingJoinPoint joinPoint) throws Throwable {
        System.out.println("我的类被切了");
        return joinPoint.proceed();
    }
}
