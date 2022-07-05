package com.wuxianggujun.robotbase.ioc;

import java.util.Set;

/**
 * 容器
 *
 * @author 无相孤君
 * @date 2022/07/04
 */
public interface Container {

    <T> T getBean(Class<T> clazz);

    <T> T getBean(String name);

    Object registerBean(Object object);

    Object registerBean(Class<?> clazz);

    Object registerBean(String name, Class<?> clazz);

    void remove(Class<?> clazz);

    void remove(String name);

    Set<String> getBeanNames();

    /**
     * 初始化装配
     */
    void initWired();
}
