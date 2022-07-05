package com.wuxianggujun.robotbase.ioc;

import java.util.Set;

public class SampleContainer implements Container{
    @Override
    public <T> T getBean(Class<T> clazz) {
        return null;
    }

    @Override
    public <T> T getBean(String name) {
        return null;
    }

    @Override
    public Object registerBean(Object object) {
        return null;
    }

    @Override
    public Object registerBean(Class<?> clazz) {
        return null;
    }

    @Override
    public Object registerBean(String name, Class<?> clazz) {
        return null;
    }

    @Override
    public void remove(Class<?> clazz) {

    }

    @Override
    public void remove(String name) {

    }

    @Override
    public Set<String> getBeanNames() {
        return null;
    }

    @Override
    public void initWired() {

    }
}
