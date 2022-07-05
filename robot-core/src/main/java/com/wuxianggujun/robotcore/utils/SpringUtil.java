package com.wuxianggujun.robotcore.utils;

import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;

public class SpringUtil {
    private static ApplicationContext applicationContext = null;
    private static Environment environment = null;

    public static void setApplicationContext(ApplicationContext applicationContext) {
        if (SpringUtil.applicationContext == null) {
            SpringUtil.applicationContext = applicationContext;
            environment = applicationContext.getEnvironment();
            //设置后，可以进行一些其他操作
        }
    }

    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    public static Environment getEnvironment() {
        return environment;
    }

    public static Object getBean(String name) {
        return getApplicationContext().getBean(name);
    }

    public static <T> T getBean(Class<T> clazz) {
        return getApplicationContext().getBean(clazz);
    }

    public static <T> T getBean(String name, Class<T> clazz) {
        return getApplicationContext().getBean(name, clazz);
    }

    public static String getString(String key) {
        return environment.getProperty(key);
    }

}
