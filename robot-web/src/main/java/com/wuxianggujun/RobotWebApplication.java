package com.wuxianggujun;

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
        WebSocketClient.main(args);
    }
}
