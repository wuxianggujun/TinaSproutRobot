package com.wuxianggujun.robotcore.core.api;

/**
 * 接收发送信息返回的Body
 */
public class ApiResult {

    private Object data;
    private int retCode;
    private String status;

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public int getRetCode() {
        return retCode;
    }

    public void setRetCode(int retCode) {
        this.retCode = retCode;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}