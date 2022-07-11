package com.wuxianggujun.robotbase.bean;

import com.wuxianggujun.robotbase.annotation.Aspect;
import com.wuxianggujun.robotbase.annotation.AutoWired;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class BeanFactory {
    private static final Map<Class<?>, Object> beans = new ConcurrentHashMap<>();
    /**
     * 带有@AutoWired 注解修饰的类
     */
    private static final Set<Class<?>> beansHasAutoWiredField = Collections.synchronizedSet(new HashSet<>());

    public static Object getBean(Class<?> clazz) {
        return beans.get(clazz);
    }

    public static void initBeans(List<Class<?>> classList) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        /**
         * 这里需要创建容器，不能修改原引用
         */
        List<Class<?>> classesToCreate = new ArrayList<>(classList);
        //被Aspect修饰的类
        List<Class<?>> aspectClasses = new ArrayList<>();
        for (Class<?> aClass : classesToCreate) {
            if (aClass.isAnnotationPresent(Aspect.class)) {
                aspectClasses.add(aClass);
            } else {
                createBean(aClass);
            }
        }
        //使用动态代理创建AOP
        //resolveAop(aspectClasses);
        // 有的类中某个属性已经通过 @AutoWired 注入了旧的被代理的对象,重新创建它们
        for (Class<?> aClass : beansHasAutoWiredField) {
            createBean(aClass);
        }
    }

    private static void createBean(Class<?> aClass) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        //初始化对象
        Object bean = aClass.getDeclaredConstructor().newInstance();
        // 遍历类中所有定义的属性，如果属性带有 @AutoWired 注解，则需要注入对应依赖
        for (Field field : aClass.getDeclaredFields()) {
            if (!field.isAnnotationPresent(AutoWired.class)) {
                continue;
            }
            //将需要注入其他的Bean的类，保存下来，因为AOP代理类生成之后，需要更新他们
            BeanFactory.beansHasAutoWiredField.add(aClass);
            Class<?> fieldType = field.getType();
            field.setAccessible(true);
            if (fieldType.isInterface()) {
                //如果依赖的类型是接口，则查询其实现类
                for (Class<?> key : BeanFactory.beans.keySet()) {
                    if (fieldType.isAssignableFrom(key)) {
                        fieldType = key;
                        break;
                    }
                }
            }
            field.set(bean, BeanFactory.getBean(fieldType));
        }
        // todo 这里可能AutoWired注入失败，例如存在循环依赖，或者bean工厂中根本不存在，目前暂时先不处理
        beans.put(aClass, bean);
    }

    /**
     * 对于所有被 @Aspect 注解修饰的类，
     * 遍历他们定义的方法，处理 @Pointcut、@Before 以及 @After 注解
     */
//    private static void resolveAOP(List<Class<?>> aspectClasses)
//            throws ClassNotFoundException, IllegalAccessException, InstantiationException {
//        for (Class<?> aClass : aspectClasses) {
//            Method before = null; // 前置动作
//            Method after = null; // 后置动作
//            String method = null; // 切入方法
//            Object target = null;
//            String pointcutName = null;
//
//            // 初始化对象，简单起见，这里先假定每一个代理类，
//            // 并且最多只有一个切点，一个前置以及一个后置处理器，所以我们也必需先处理 pointcut，再解析before和after方法
//            Object bean = aClass.newInstance();
//            Method[] methods = aClass.getDeclaredMethods();
//            for (Method m : methods) {
//                if (m.isAnnotationPresent(Pointcut.class)) {
//                    // com.caozhihu.demo.Rapper.rap()
//                    String pointcutValue = m.getAnnotation(Pointcut.class).value();
//                    // 截取全限定类名
//                    String classStr = pointcutValue.substring(0, pointcutValue.lastIndexOf("."));
//                    target = Thread.currentThread().getContextClassLoader().loadClass(classStr).newInstance();
//                    method = pointcutValue.substring(pointcutValue.lastIndexOf(".") + 1);
//                    pointcutName = m.getName();
//                }
//            }
//            // 如果没有切点名，则返回
//            if (pointcutName == null) continue;
//
//            for (Method m : bean.getClass().getDeclaredMethods()) {
//                if (m.isAnnotationPresent(Before.class)) {
//                    String value = m.getAnnotation(Before.class).value();
//                    if (takeToBrackets.apply(value).equals(pointcutName)) {
//                        before = m;
//                    }
//                } else if (m.isAnnotationPresent(After.class)) {
//                    String value = m.getAnnotation(After.class).value();
//                    if (takeToBrackets.apply(value).equals(pointcutName)) {
//                        after = m;
//                    }
//                }
//            }
//
//            // 获取代理对象并更新 bean 工厂
//            Object proxy = new ProxyDyna().createProxy(bean, before, after,
//                    target, takeToBrackets.apply(method));
//            BeanFactory.beans.put(target.getClass(), proxy);
//        }
//    }

}
