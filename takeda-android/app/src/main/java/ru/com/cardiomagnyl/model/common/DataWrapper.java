package ru.com.cardiomagnyl.model.common;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class DataWrapper {

    public static ObjectNode wrap(JsonNode data) {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode objectNode = mapper.createObjectNode();
        objectNode.set("data", data);
        return objectNode;
    }

    public static ObjectNode wrap(String data) {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode objectNode = mapper.createObjectNode();
        objectNode.put("data", data);
        return objectNode;
    }

}
