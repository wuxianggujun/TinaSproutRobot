package com.wuxianggujun.robotcore.core.api;

import cn.hutool.http.HttpUtil;

import java.util.HashMap;
import java.util.Map;

public abstract class ApiSend {
    private final String url = "http://wuxianggujun.com:8000/";
    private final String HEADER_CONTENT_TYPE = "Content-Type";
    private final String CHARSET_UTF8 = "charset=UTF-8";
    private final String APPLICATION_JSON = "application/json";
    private final String APPLICATION_JSON_CHARSET = "application/json;charset=UTF-8";
    private final Map<String, String> headers;

    public ApiSend() {
        if (getHeaders() == null) {
            headers = new HashMap<>();
            headers.put(HEADER_CONTENT_TYPE, APPLICATION_JSON_CHARSET);
        } else {
            headers = getHeaders();
        }
    }


    public abstract Map<String, Object> getParams();

     public Map<String, String> getHeaders() {
        return headers;
    }


    public void send(String actions) {
        String body = HttpUtil.createGet(url + actions).addHeaders(headers).form(getParams()).execute().body();
        System.out.println("这样子不会有很多："+body);

    }

}