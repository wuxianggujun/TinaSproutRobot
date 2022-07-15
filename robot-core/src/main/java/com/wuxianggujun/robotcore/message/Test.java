package com.wuxianggujun.robotcore.message;

import com.wuxianggujun.robotcore.core.api.ApiSend;

public class Test {
    public static void main(String[] args) {
        ApiSend sendMessage = new SendMessage();
        sendMessage.send("send_msg");
    }
}