package com.wuxianggujun;

import com.wuxianggujun.robot.event.RegisterEventListener;
import com.wuxianggujun.robotbase.bean.BeanFactory;
import com.wuxianggujun.robotbase.core.ClassScanner;
import com.wuxianggujun.robotcore.core.framework.WebSocketClient;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;

@SpringBootApplication(scanBasePackages = "com.wuxianggujun")
public class RobotWebApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(RobotWebApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        // 扫描类
        List<Class<?>> classList = ClassScanner.scannerCLasses(RobotWebApplication.class.getPackage().getName());
        System.out.println(Arrays.asList(classList));
        // 初始化 Bean 工厂,初始化 AOP，这里使用了 JDK 动态代理，
        // Bean工厂第一次初始化后，使用代理类的对象来覆盖 Bean 工厂中的对应对象
        BeanFactory.initBean(classList);

        RegisterEventListener.register();
        WebSocketClient.main(args);
    }
}