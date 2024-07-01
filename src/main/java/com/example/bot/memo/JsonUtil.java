package com.example.bot.memo;

import com.example.bot.memo.command.CallbackData;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonUtil {
    private static final ObjectMapper mapper = new ObjectMapper();

    public static String toJson(CallbackData data) {
        try {
            return mapper.writeValueAsString(data);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public static CallbackData fromJson(String json) {
        try {
            return mapper.readValue(json, CallbackData.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
