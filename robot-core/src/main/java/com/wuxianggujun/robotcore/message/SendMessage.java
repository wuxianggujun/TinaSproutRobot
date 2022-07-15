package com.wuxianggujun.robotcore.message;

import com.wuxianggujun.robotcore.core.api.ApiSend;

import java.util.HashMap;
import java.util.Map;

public class SendMessage extends ApiSend {
    @Override
    public Map<String, Object> getParams() {
        Map<String, Object> params = new HashMap<>();
        params.put("user_id", 3344207732L);
        params.put("message_type", "private");
        params.put("message", "你好！我是缇娜-斯普朗特");
        params.put("auto_escape", false);
        return params;
    }

    @Override
    public Map<String, String> getHeaders() {
        
        return super.getHeaders();
    }
}