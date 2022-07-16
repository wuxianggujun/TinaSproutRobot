package com.wuxianggujun.robotcore.message;

import com.wuxianggujun.robotcore.core.api.BaseApi;
import com.wuxianggujun.robotcore.core.api.TestApi;
import com.wuxianggujun.robotcore.utils.JsonUtil;

import java.time.LocalDate;

public class Test {
    public static void main(String[] args) {
        BaseApi test = new TestApi();
        System.out.println(test.buildJson());
    }
}