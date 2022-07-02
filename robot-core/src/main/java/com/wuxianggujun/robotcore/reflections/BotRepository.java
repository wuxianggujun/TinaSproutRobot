package com.wuxianggujun.robotcore.reflections;

import com.wuxianggujun.robotcore.annotation.Bot;
import org.reflections.Reflections;
import org.reflections.scanners.Scanners;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.reflections.util.FilterBuilder;

import java.lang.reflect.Field;
import java.util.Set;

public class BotRepository {
    private final Reflections reflections;
    private Set<Field> botFieldSet;
    private Set<Class<?>> botClassSet;

    public BotRepository(String packagePath, Scanners... scanners) {
        ConfigurationBuilder configurationBuilder = new ConfigurationBuilder()
                .addUrls(ClasspathHelper.forPackage(packagePath))
                .addScanners(scanners)
                .filterInputsBy(new FilterBuilder().includePattern("java.*"));
        //过滤掉java.*下的类
        this.reflections = new Reflections(configurationBuilder);
    }

    public Set<Field> getBotFields() {
        if (botFieldSet == null) {
            botFieldSet = reflections.getFieldsAnnotatedWith(Bot.class);
        }
        return botFieldSet;
    }

    public Set<Class<?>> getBotClass() {
        if (botClassSet == null) {
            botClassSet = reflections.getTypesAnnotatedWith(Bot.class);
        }
        return botClassSet;
    }

    public void getBotSubTypes() {

    }

}
