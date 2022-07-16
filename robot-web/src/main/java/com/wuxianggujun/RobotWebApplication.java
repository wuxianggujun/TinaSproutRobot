package com.wuxianggujun;

import com.wuxianggujun.robot.event.RegisterEventListener;
import com.wuxianggujun.robotcore.core.bot.BotClient;
import com.wuxianggujun.robotcore.core.framework.WebSocketClient;
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
        BotClient botClient = new BotClient("ws://wuxianggujun.com:8001/websocket");
    }
}