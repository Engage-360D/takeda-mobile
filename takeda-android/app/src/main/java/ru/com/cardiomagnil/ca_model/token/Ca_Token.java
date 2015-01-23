package ru.com.cardiomagnil.ca_model.token;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import ru.com.cardiomagnil.ca_model.base.BaseModel;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "id",
        "links"
})
@DatabaseTable(tableName = "token")
public class Ca_Token extends BaseModel {

    @DatabaseField(dataType = DataType.STRING, columnName = "token_id")
    @JsonProperty("id")
    private String id;

    // links
    @DatabaseField(id = true, canBeNull = false, dataType = DataType.STRING, columnName = "user_id")
    @JsonProperty("user")
    private String user;

    @JsonProperty("links")
    private JsonNode links;

    /**
     * @return The id
     */
    @JsonProperty("id")
    public String getTokenId() {
        return id;
    }

    /**
     * @param id The id
     */
    @JsonProperty("id")
    public void setTokenId(String id) {
        this.id = id;
    }

    /**
     * @return The region
     */
    @JsonProperty("user")
    public String getUserId() {
        return user;
    }

    /**
     * @param user The user
     */
    @JsonProperty("user")
    public void setUserId(String user) {
        this.user = user;
    }

    /**
     * @return The links
     */
    @JsonProperty("links")
    public JsonNode getLinks() {
        return links;
    }

    /**
     * @param links The links
     */
    @JsonProperty("links")
    public void setLinks(JsonNode links) {
        this.links = links;
    }

    public static void unPackLinks(ObjectNode objectNodePacked) {
        if (objectNodePacked != null) {
            JsonNode jsonNode = objectNodePacked.get("links");
            objectNodePacked.remove("links");
            objectNodePacked.putAll((ObjectNode) jsonNode);
        }
    }

    public static void packLinks(ObjectNode objectNodeUnpacked) {
        if (objectNodeUnpacked != null) {
            ObjectNode nodeLinks = JsonNodeFactory.instance.objectNode();
            nodeLinks.put("user", objectNodeUnpacked.get("user"));
            objectNodeUnpacked.remove("user");
            objectNodeUnpacked.put("links", nodeLinks);
        }
    }

}
