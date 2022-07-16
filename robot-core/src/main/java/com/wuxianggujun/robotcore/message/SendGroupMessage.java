package com.wuxianggujun.robotcore.message;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.wuxianggujun.robotcore.core.api.BaseApi;

public class SendGroupMessage extends BaseApi {
    private final SendGroupMessage.Param param;

    public SendGroupMessage(long groupId, String message) {
        this.param = new SendGroupMessage.Param();
        this.param.setGroupId(groupId);
        this.param.setMessage(message);
        this.param.setAutoEscape(false);
    }


    @Override
    public String getAction() {
        return "/send_group_msg";
    }

    @Override
    public Object getParams() {
        return param;
    }

    public static class Param {

        @JsonProperty("group_id")
        private long groupId;

        @JsonProperty("message")
        private Object message;

        @JsonProperty("auto_escape")
        private boolean autoEscape;

        public long getGroupId() {
            return groupId;
        }

        public void setGroupId(long groupId) {
            this.groupId = groupId;
        }

        public Object getMessage() {
            return message;
        }

        public void setMessage(Object message) {
            this.message = message;
        }

        public boolean isAutoEscape() {
            return autoEscape;
        }

        public void setAutoEscape(boolean autoEscape) {
            this.autoEscape = autoEscape;
        }
    }
}