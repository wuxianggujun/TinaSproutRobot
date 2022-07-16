package com.wuxianggujun.robotcore.core.api;

public class TestApi extends BaseApi{
    @Override
    public String getAction() {
        return "Test";
    }

    @Override
    public Object getParams() {
        return "giao";
    }
}