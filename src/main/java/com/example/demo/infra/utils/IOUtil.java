package com.example.demo.infra.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

/**
 * InputStream to String
 * https://www.baeldung.com/convert-input-stream-to-string
 */
public class IOUtil {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static String inputStreamToString(InputStream inputStream) {
        StringBuilder textBuilder = new StringBuilder();
        try (Reader reader = new BufferedReader(new InputStreamReader
                (inputStream, Charset.forName(StandardCharsets.UTF_8.name())))) {
            int c;
            while ((c = reader.read()) != -1) {
                textBuilder.append((char) c);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return textBuilder.toString();
    }

    public static List<Map<String, Object>> stringToList(String contents) {
        try {
            return objectMapper.readValue(contents, List.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static List<Map<String, Object>> inputStreamToList(InputStream inputStream) {
        return stringToList(inputStreamToString(inputStream));
    }
}
