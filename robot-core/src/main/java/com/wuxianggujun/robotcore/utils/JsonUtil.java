package com.wuxianggujun.robotcore.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

public class JsonUtil {
    private static ObjectMapper objectMapper = new ObjectMapper();

    static {
        JavaTimeModule timeModule = new JavaTimeModule();
        // 设置LocalDateTime的序列化格式
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        timeModule.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(dateTimeFormatter));
        objectMapper.registerModule(timeModule);
    }

    public static <T> String obj2String(T obj) {
        if(obj == null){
            return null;
        }
        String s = null;
        try {
            s = obj instanceof String ? (String)obj :  objectMapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return s;
    }

    public static <T> String obj2StringPretty(T obj) {
        if (obj == null) {
            return null;
        }
        try {
            return obj instanceof String ? (String) obj : objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            return null;
        }
    }

    public static <T> T string2Obj(String str, Class<T> clazz) {
        if(str == null || str.length()==0 || clazz == null){
            return null;
        }
        T t = null;
        try {
            t = clazz.equals(String.class)? (T)str : objectMapper.readValue(str,clazz);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return t;
    }

    /**
     * 在字符串与集合对象转换时使用
     */
    public static <T> T string2Obj(String str, TypeReference<T> typeReference) {
        if (str ==null || str.length() ==0 || typeReference == null) {
            return null;
        }
        try {
            return (T) (typeReference.getType().equals(String.class) ? str : objectMapper.readValue(str, typeReference));
        } catch (IOException e) {
            return null;
        }
    }

    /**
     * 在字符串与集合对象转换时使用
     */
    public static <T> T string2Obj(String str, Class<?> collectionClazz, Class<?>... elementClazzes) {
        JavaType javaType = objectMapper.getTypeFactory().constructParametricType(collectionClazz, elementClazzes);
        try {
            return objectMapper.readValue(str, javaType);
        } catch (IOException e) {
            return null;
        }
    }

    //亮点：模拟构造方法设计模式提供类似于阿里巴巴FastJSON的put方式构造JSON功能
    public static JsonUtil.JsonBuilder builder() {
        return new JsonUtil.JsonBuilder();
    }

    public static class JsonBuilder {
        private Map<String ,Object> map = new HashMap<>();

        JsonBuilder() {
        }
        public JsonUtil.JsonBuilder put(String key ,Object value){
            map.put(key,value);
            return this;
        }

        public String build() {

            //ObjectMapper objectMapper = new ObjectMapper();
            //ObjectMapper objectMapper = JsonUtil.objectMapper;
            
            try {
                return objectMapper.writeValueAsString(this.map);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
            return "{}";
        }
    }
}