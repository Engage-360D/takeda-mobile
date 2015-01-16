package ru.com.cardiomagnil.ca_model.common;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class Ca_DataWraper {

    public static ObjectNode wrap(JsonNode data) {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode objectNode = mapper.createObjectNode();
        objectNode.set("data", data);
        return objectNode;
    }

}
