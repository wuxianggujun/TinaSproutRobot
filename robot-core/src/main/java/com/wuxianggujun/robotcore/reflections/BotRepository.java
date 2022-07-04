package com.wuxianggujun.robotcore.reflections;

import com.wuxianggujun.robotbase.cache.ObjectCache;
import com.wuxianggujun.robotcore.annotation.BotAnnotation;
import com.wuxianggujun.robotcore.annotation.MessageEvent;
import com.wuxianggujun.robotcore.core.bot.Bot;
import com.wuxianggujun.robotcore.enums.MessageEventType;
import com.wuxianggujun.robotcore.listener.MessageListener;
import com.wuxianggujun.robotcore.listener.impl.GroupMessageListener;
import org.reflections.Reflections;
import org.reflections.scanners.Scanners;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.reflections.util.FilterBuilder;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class BotRepository {
    private final Reflections reflections;
    private Set<Field> botFieldSet;
    private Set<Class<?>> botClassSet;

    public BotRepository(String packagePath, Scanners... scanners) {
        ConfigurationBuilder configurationBuilder = new ConfigurationBuilder()
                .addUrls(ClasspathHelper.forPackage(packagePath))
                .addScanners(scanners);
        //.filterInputsBy(new FilterBuilder().includePattern("java.*"));
        //过滤掉java.*下的类
        this.reflections = new Reflections(configurationBuilder);
    }

    public Set<Field> getBotFields() {
        if (botFieldSet == null) {
            botFieldSet = reflections.getFieldsAnnotatedWith(BotAnnotation.class);
        }
        return botFieldSet;
    }

    public Set<Class<?>> getBotClass() {
        if (botClassSet == null) {
            botClassSet = reflections.getTypesAnnotatedWith(BotAnnotation.class);
        }
        return botClassSet;
    }

    public void getBotSubTypes() {
        Reflections reflections1 = new Reflections(new ConfigurationBuilder()
                .forPackages("com.wuxianggujun")
                .setScanners(Scanners.values())
                .filterInputsBy(new FilterBuilder()
                        .includePackage("com.wuxianggujun")
                        .excludePackage("com.wuxianggujun.robotcore")
                        .excludePackage("com.wuxianggujun.robotbase")));
        Set<Class<? extends MessageListener>> classSet = reflections1.getSubTypesOf(MessageListener.class);
        for (Class<? extends MessageListener> clazz : classSet) {
            //System.out.println("cao: " + clazz.getName());
        }
        //可以用来判断是不是某个接口的实现类然后执行添加操作
        List<Class<? extends MessageListener>> filters = classSet.stream()
                .filter(aClass -> !Modifier.isAbstract(aClass.getModifiers())).collect(Collectors.toList());
        for (Class<? extends MessageListener> clazz : filters) {

            System.out.println(clazz.getName());
            MessageEvent messageEvent = clazz.getAnnotation(MessageEvent.class);
            if (messageEvent != null) {
                MessageEventType messageEventType = messageEvent.value();
                System.out.println(messageEventType);

            }

            System.out.println(clazz.getName());
            System.out.println(clazz.getAnnotation(MessageEvent.class));
            //判断是不是GroupMessageListener,继承的接口是没有用的
            if (GroupMessageListener.class.isAssignableFrom(clazz)) {

                try {
                    //我现在应该获取枚举类型
//                    Bot bot = clazz.getDeclaredAnnotation(Bot.class);
//                    System.out.println("nb" + bot.value());
                    //System.out.println(clazz.getAnnotation(Bot.class).value());
                    //System.out.println(clazz.getDeclaredConstructor().newInstance());
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }

            //System.out.println("list: " + clazz.getName());
        }
    }

    public void getBotField() throws IllegalAccessException, NoSuchMethodException, InvocationTargetException, InstantiationException {
        Reflections reflections = new Reflections(new ConfigurationBuilder()
                .forPackages("com.wuxianggujun.robotweb")
                .setScanners(Scanners.values()));
        //Set<Class<?>> botClass = reflections.getTypesAnnotatedWith()
        Set<Field> botFieldSet = reflections.getFieldsAnnotatedWith(BotAnnotation.class);

        for (Field field : botFieldSet) {
            //System.out.println(field);
            //如果指定类型的注释存在于此元素上,否则返回false。这种方法的设计主要是为了方便访问标记注释.
            if (field.isAnnotationPresent(BotAnnotation.class)) {
                Object object = field.getDeclaringClass().getDeclaredConstructor().newInstance();
                if (!field.canAccess(object)) {
                    field.setAccessible(true);
                }
                Class<?> botClass = field.getType();
                if (Bot.class.equals(botClass)) {
                    //创建了bot对象
                    Bot bot = (Bot) botClass.getDeclaredConstructor().newInstance();
                    bot.setAge(120);
                    bot.setName("无相");
                    //接下来要获取注解所在的类
                    //返回表示声明此对象所表示的字段的类或接口的对象
                    field.get(object);
                    field.set(object, bot);
                    ObjectCache.getInstance().putCache(field.getDeclaringClass().getName(), object);
                    System.out.println(object);
                }

            }
        }
    }

}
