package com.wuxianggujun.robotcore.core.api;

import com.wuxianggujun.robotcore.utils.JsonUtil;

import java.util.HashMap;
import java.util.Map;

public abstract class BaseApi {
    public abstract String getAction();

    public abstract Object getParams();

    private String echo = null;

    public String getEcho() {
        return this.echo;
    }

    public String buildJson() {
        Map<String, Object> map = new HashMap<>();
        map.put("actions", this.getAction());
        map.put("params", this.getParams());
        //map.put("echo", this.getEcho());
        return JsonUtil.toJsonString(map);
    }

}