package com.wuxianggujun.robotcore.listener.message;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 私信消息
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class PrivateMessageEvent extends MessageEvent {
    @JsonProperty("sub_type")
    private String subType;

    @JsonProperty("sender")
    private PrivateSender sender;

    @JsonProperty("message_id")
    private long messageId;


    @JsonProperty("target_id")
    private long targetId;

    public String getSubType() {
        return subType;
    }

    public void setSubType(String subType) {
        this.subType = subType;
    }

    public PrivateSender getSender() {
        return sender;
    }

    public void setSender(PrivateSender sender) {
        this.sender = sender;
    }

    public long getMessageId() {
        return messageId;
    }

    public void setMessageId(long messageId) {
        this.messageId = messageId;
    }


    public long getTargetId() {
        return targetId;
    }

    public void setTargetId(long targetId) {
        this.targetId = targetId;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class PrivateSender {

        private int age;
        private String nickname;
        private String sex;
        @JsonProperty("user_id")
        private long userId;

        public void setAge(int age) {
            this.age = age;
        }

        public int getAge() {
            return age;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public String getNickname() {
            return nickname;
        }

        public void setSex(String sex) {
            this.sex = sex;
        }

        public String getSex() {
            return sex;
        }

        public void setUserId(long userId) {
            this.userId = userId;
        }

        public long getUserId() {
            return userId;
        }
    }

}
