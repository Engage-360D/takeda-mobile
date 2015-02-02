package ru.com.cardiomagnil.model.base;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonAutoDetect(fieldVisibility = Visibility.NONE,
/*            */getterVisibility = Visibility.NONE,
/*            */setterVisibility = Visibility.NONE,
/*            */creatorVisibility = Visibility.NONE)
public abstract class BaseModel {
    @JsonCreator
    public BaseModel() {
    }

    @JsonCreator
    public BaseModel(String jsonString) {
        jsonToObjectConstructor(jsonString);
    }

    private void jsonToObjectConstructor(String jsonString) {
        try {
            ObjectMapper objectMapper = JacksonNonBlockingObjectMapperFactory
                    .getObjectMapper()
                    .enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);
            objectMapper.readerForUpdating(this).readValue(jsonString);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("rawtypes")
    public static Object stringToObject(String jsonString, TypeReference typeReference) {
        Object resultObject = null;
        try {
            resultObject = JacksonNonBlockingObjectMapperFactory
                    .getObjectMapper()
                    .enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
                    .readValue(jsonString, typeReference);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultObject;
    }

    @Override
    public String toString() {
        try {
            OutputStream stream = new ByteArrayOutputStream();
            new ObjectMapper().writeValue(stream, this);
            return stream.toString();
        } catch (Exception e) {
            return super.toString();
        }
    }

    public String emptyOnNull(String text) {
        return text != null ? text : "";
    }

    protected static JsonNode stringToJsonNode(String string) {
        JsonNode jsonNode = null;
        try {
            ObjectMapper mapper = new ObjectMapper();
            jsonNode = mapper.readTree(string);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return jsonNode;
    }

}

