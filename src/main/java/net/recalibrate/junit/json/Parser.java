package net.recalibrate.junit.json;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.NonNull;

import java.io.IOException;
import java.io.InputStream;

/**
 * Class for handling parsing via jackson's {@link ObjectMapper}; primarily exists to facilitate easier testing.
 */
@AllArgsConstructor
public class Parser {
    @NonNull
    private final ObjectMapper mapper;


    public Object parseFromString(String json, Class<?> clazz) throws JsonProcessingException {
        return mapper.readValue(json, clazz);
    }

    public Object parseFromResource(String resourceLocation, Class<?> clazz) throws IOException {
        try (InputStream is = getClass().getResourceAsStream(resourceLocation)) {
            return mapper.readValue(is, clazz);
        }
    }
}
