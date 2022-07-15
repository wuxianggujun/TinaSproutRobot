package com.wuxianggujun.robotcore.core.api.entiy;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Data {
    @JsonProperty("message_id")
    private int messageId;

    public int getMessageId() {
        return messageId;
    }

    public void setMessageId(int messageId) {
        this.messageId = messageId;
    }
}