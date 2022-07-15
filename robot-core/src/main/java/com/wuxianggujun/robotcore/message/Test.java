package com.wuxianggujun.robotcore.message;

import com.wuxianggujun.robotcore.utils.JsonUtil;

import java.time.LocalDate;

public class Test {
    public static void main(String[] args) {
//        ApiSend sendMessage = new SendMessage();
//        sendMessage.send("send_msg");
//        
//        SendGroupForwardMessage sendGroupForwardMessage = new SendGroupForwardMessage();
//        sendGroupForwardMessage.send("send_group_forward_msg");
        String json = JsonUtil.builder()
                .put("id", 123)
                .put("name", "zhangsan")
                .put("birth", LocalDate.now())
                .put("gender", true)
                .build();
        System.out.println(json);
    }
}