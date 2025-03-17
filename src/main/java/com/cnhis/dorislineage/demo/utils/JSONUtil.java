package com.cnhis.dorislineage.demo.utils;

import com.cnhis.dorislineage.demo.expection.JsonException;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * JSON 工具类
 */
public final class JSONUtil {

    private static ObjectMapper objectMapper = ApplicationContextHelper.getContext().getBean(ObjectMapper.class);

    private JSONUtil() throws IllegalAccessException {
        throw new IllegalAccessException("Cannot Be Accessed!");
    }

    public static <T> T toObj(String json, Class<T> clazz) {
        try {
            return objectMapper.readValue(json, clazz);
        } catch (IOException var3) {
            throw new JsonException("error.jackson.read", var3);
        }
    }

    public static <T> List<T> toArray(String json, Class<T> clazz) {
        try {
            CollectionType type = objectMapper.getTypeFactory().constructCollectionType(ArrayList.class, clazz);
            return objectMapper.readValue(json, type);
        } catch (IOException var3) {
            throw new JsonException("error.jackson.read", var3);
        }
    }

    public static <T> String toJson(T obj) {
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (IOException e) {
            throw new JsonException("error.jackson.write", e);
        }
    }

    public static <T> String mapToJson(T obj) {
        try {
            objectMapper.setSerializationInclusion(Include.ALWAYS);
            String json = objectMapper.writeValueAsString(obj);
            objectMapper.setSerializationInclusion(Include.USE_DEFAULTS);
            return json;
        } catch (IOException e) {
            throw new JsonException("error.jackson.write", e);
        }
    }
}