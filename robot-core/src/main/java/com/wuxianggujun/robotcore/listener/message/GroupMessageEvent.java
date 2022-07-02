package com.wuxianggujun.robotcore.listener.message;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.wuxianggujun.robotcore.listener.common.Anonymous;

/**
 * 群消息
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class GroupMessageEvent extends MessageEvent {
    @JsonProperty("meta_event_type")
    private String metaEventType;

    @JsonProperty("sub_type")
    private String subType;
    @JsonProperty("message_id")
    private int messageId;
    @JsonProperty("anonymous")
    private Anonymous anonymous;
    @JsonProperty("group_id")
    private int groupId;
    @JsonProperty("message_seq")
    private int messageSeq;

    @JsonProperty("sender")
    private GroupSender sender;

    public String getMetaEventType() {
        return metaEventType;
    }

    public void setMetaEventType(String metaEventType) {
        this.metaEventType = metaEventType;
    }

    public String getSubType() {
        return subType;
    }

    public void setSubType(String subType) {
        this.subType = subType;
    }

    public int getMessageId() {
        return messageId;
    }

    public void setMessageId(int messageId) {
        this.messageId = messageId;
    }

    public Anonymous getAnonymous() {
        return anonymous;
    }

    public void setAnonymous(Anonymous anonymous) {
        this.anonymous = anonymous;
    }

    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    public int getMessageSeq() {
        return messageSeq;
    }

    public void setMessageSeq(int messageSeq) {
        this.messageSeq = messageSeq;
    }

    public GroupSender getSender() {
        return sender;
    }

    public void setSender(GroupSender sender) {
        this.sender = sender;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class GroupSender {
        @JsonProperty("age")
        private int age;
        @JsonProperty("area")
        private String area;
        @JsonProperty("card")
        private String card;
        @JsonProperty("level")
        private String level;
        @JsonProperty("nickname")
        private String nickname;
        @JsonProperty("role")
        private String role;
        @JsonProperty("sex")
        private String sex;
        @JsonProperty("title")
        private String title;
        @JsonProperty("user_id")
        private long userId;

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }

        public String getArea() {
            return area;
        }

        public void setArea(String area) {
            this.area = area;
        }

        public String getCard() {
            return card;
        }

        public void setCard(String card) {
            this.card = card;
        }

        public String getLevel() {
            return level;
        }

        public void setLevel(String level) {
            this.level = level;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public String getRole() {
            return role;
        }

        public void setRole(String role) {
            this.role = role;
        }

        public String getSex() {
            return sex;
        }

        public void setSex(String sex) {
            this.sex = sex;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public long getUserId() {
            return userId;
        }

        public void setUserId(long userId) {
            this.userId = userId;
        }
    }

}
