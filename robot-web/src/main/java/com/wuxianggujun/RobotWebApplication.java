package com.wuxianggujun;

import cn.hutool.extra.spring.SpringUtil;
import com.wuxianggujun.robot.event.RegisterEventListener;
import com.wuxianggujun.robotcore.core.bot.BotClient;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.wuxianggujun")
public class RobotWebApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(RobotWebApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        RegisterEventListener.register();
        BotClient botClient = SpringUtil.getBean(BotClient.class);
        botClient.connection();
    }
}