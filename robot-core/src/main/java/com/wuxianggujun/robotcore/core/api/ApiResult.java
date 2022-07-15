package com.wuxianggujun.robotcore.core.api;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 接收发送信息返回的Body
 */
public class ApiResult {

    private Data data;
    private int retCode;
    private String status;

    private class Data {
        @JsonProperty("message_id")
        private int messageId;

        public int getMessageId() {
            return messageId;
        }

        public void setMessageId(int messageId) {
            this.messageId = messageId;
        }
    }
}