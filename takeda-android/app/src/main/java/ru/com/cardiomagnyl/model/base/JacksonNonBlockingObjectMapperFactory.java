package ru.com.cardiomagnyl.model.base;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.core.io.NumberInput;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;

import java.io.IOException;

// need this to catch "false" values on integer fields
public class JacksonNonBlockingObjectMapperFactory {

    private static class NonBlockingDeserializer extends JsonDeserializer<Integer> {
        final protected Class<?> _valueClass = Integer.class;

        @Override
        public Integer deserialize(JsonParser jp, DeserializationContext context) throws IOException, JsonProcessingException {
            try {
                return _parseInteger(jp, context);
            } catch (Exception e) {
                // If a JSON Mapping occurs, simply returning null instead of blocking things
                return 0;
            }
        }

        @Override
        public Integer getNullValue() {
            return 0;
        }

        protected final Integer _parseInteger(JsonParser jp, DeserializationContext context)
                throws IOException, JsonProcessingException {
            JsonToken t = jp.getCurrentToken();
            if (t == JsonToken.VALUE_NUMBER_INT || t == JsonToken.VALUE_NUMBER_FLOAT) { // coercing should work too
                return Integer.valueOf(jp.getIntValue());
            }
            if (t == JsonToken.VALUE_STRING) { // let's do implicit re-parse
                String text = jp.getText().trim();
                try {
                    int len = text.length();
                    if ("null".equals(text)) {
                        return (Integer) getNullValue();
                    }
                    if (len > 9) {
                        long l = Long.parseLong(text);
                        if (l < Integer.MIN_VALUE || l > Integer.MAX_VALUE) {
                            throw context.weirdStringException(text, _valueClass,
                                    "Overflow: numeric value (" + text + ") out of range of Integer (" + Integer.MIN_VALUE + " - " + Integer.MAX_VALUE + ")");
                        }
                        return Integer.valueOf((int) l);
                    }
                    if (len == 0) {
                        return (Integer) getEmptyValue();
                    }
                    return Integer.valueOf(NumberInput.parseInt(text));
                } catch (IllegalArgumentException iae) {
                    throw context.weirdStringException(text, _valueClass, "not a valid Integer value");
                }
            }
            if (t == JsonToken.VALUE_NULL) {
                return (Integer) getNullValue();
            }
            // Otherwise, no can do:
            throw context.mappingException(_valueClass, t);
        }
    }

    public static ObjectMapper getObjectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        SimpleModule customJacksonModule = new SimpleModule();
        customJacksonModule.addDeserializer(Integer.class, new NonBlockingDeserializer());
        customJacksonModule.addDeserializer(Integer.TYPE, new NonBlockingDeserializer());
        objectMapper.registerModule(customJacksonModule);
        return objectMapper;
    }
}
