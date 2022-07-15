package com.wuxianggujun.robotcore.message;

import com.wuxianggujun.robotcore.core.api.ApiSend;

import java.util.HashMap;
import java.util.Map;

/**
 * 消息合并转发
 */
public class SendGroupForwardMessage extends ApiSend {
    public SendGroupForwardMessage(String string){
        try {
            System.out.println("SendGroup :"+string);
        }catch (Exception e){
            e.printStackTrace();
        }
      
    }

    @Override
    public Map<String, Object> getParams() {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("group_id", 537756994l);
        params.put("messages", null);
        return params;
    }
    
    

}