package com.wuxianggujun.robotcore.message;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.wuxianggujun.robotcore.core.api.BaseApi;

public class SendMessage extends BaseApi {

    private final SendMessage.Params params;

    public SendMessage(long userId, String messageType, String message, boolean auto_escape) {
        params = new SendMessage.Params();
        params.setUserId(userId);
        params.setMessageType(messageType);
        params.setMessage(message);
        params.setAuto_escape(auto_escape);
    }

    @Override
    public String getAction() {
        return "send_msg";
    }

    @Override
    public Object getParams() {
        return params;
    }

    public static class Params {

        @JsonProperty("user_id")
        private long userId;

        @JsonProperty("message_type")
        private String messageType;

        @JsonProperty("message")
        private String message;

        @JsonProperty("auto_escape")
        private boolean auto_escape;


        public long getUserId() {
            return userId;
        }

        public void setUserId(long userId) {
            this.userId = userId;
        }

        public String getMessageType() {
            return messageType;
        }

        public void setMessageType(String messageType) {
            this.messageType = messageType;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public boolean isAuto_escape() {
            return auto_escape;
        }

        public void setAuto_escape(boolean auto_escape) {
            this.auto_escape = auto_escape;
        }
    }
}