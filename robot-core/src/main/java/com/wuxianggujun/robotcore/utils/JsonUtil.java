package com.wuxianggujun.robotcore.utils;

import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.Map;

public class JsonUtil {
    private static ObjectMapper objectMapper = new ObjectMapper();

    static {
        JavaTimeModule timeModule = new JavaTimeModule();
        //对象的所有字段全部列入
        objectMapper.setSerializationInclusion(JsonInclude.Include.ALWAYS);
        //取消默认转换timestamps形式
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        //忽略空Bean转json的错误
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        // 设置LocalDateTime的序列化格式
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        timeModule.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(dateTimeFormatter));
        objectMapper.registerModule(timeModule);
        //忽略 在json字符串中存在，但是在java对象中不存在对应属性的情况。防止错误
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }


    public static <T> String obj2String(T obj) {
        if (obj == null) {
            return null;
        }
        String s = null;
        try {
            s = obj instanceof String ? (String) obj : objectMapper.writeValueAsString(obj);
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
        if (str == null || str.length() == 0 || clazz == null) {
            return null;
        }
        T t = null;
        try {
            t = clazz.equals(String.class) ? (T) str : objectMapper.readValue(str, clazz);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return t;
    }

    /**
     * 在字符串与集合对象转换时使用
     */
    public static <T> T string2Obj(String str, TypeReference<T> typeReference) {
        if (str == null || str.length() == 0 || typeReference == null) {
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

    /**
     * 序列化为JSON字符串
     *
     * @param obj obj
     * @return {@link String}
     */
    public static String toJsonString(Object obj) {
        if (obj == null) return null;
        String result = null;
        try {
            result = objectMapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return result;
    }


    /**
     * 解析对象
     * 反序列化为Object
     *
     * @param clazz   clazz
     * @param jsonStr json str
     * @return {@link T}
     */
    public static <T> T parseObject(String jsonStr, Class<T> clazz) {
        if (StrUtil.isBlank(jsonStr) || clazz == null) return null;
        T t = null;
        try {
            t = objectMapper.readValue(jsonStr, clazz);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return t;
    }


    /**
     * 解析Map
     * 反序列化为Map集合
     *
     * @param mapJsonStr 地图json str
     * @param kClazz     k clazz
     * @param vClazz     v clazz
     * @return {@link Map}<{@link K}, {@link V}>
     */
    public static <K, V> Map<K, V> parseMap(String mapJsonStr, Class<K> kClazz, Class<V> vClazz) {
        if (StrUtil.isBlank(mapJsonStr) || kClazz == null || vClazz == null) return Collections.emptyMap();
        Map<K, V> map = Collections.EMPTY_MAP;
        try {
            map = objectMapper.readValue(mapJsonStr, objectMapper.getTypeFactory().constructParametricType(Map.class, kClazz, vClazz));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return map;
    }


}