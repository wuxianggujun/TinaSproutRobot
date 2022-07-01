package com.wuxianggujun.robotcore.listener.message;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.wuxianggujun.robotcore.config.TimestampToLocalDateTimeDeserializer;

import java.io.Serializable;
import java.time.LocalDateTime;

//不存在的字段将被忽略。
@JsonIgnoreProperties(ignoreUnknown = true)
public class BaseEvent implements Serializable {

    @JsonProperty("post_type")
    private String eventType;

    @JsonProperty("self_id")
    private Long botQQ;

    //@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", locale = "zh", timezone = "GMT+8")
    @JsonProperty("time")
    @JsonDeserialize(using = TimestampToLocalDateTimeDeserializer.class)
    private LocalDateTime time;


    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public Long getBotQQ() {
        return botQQ;
    }

    public void setBotQQ(Long botQQ) {
        this.botQQ = botQQ;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }
}
