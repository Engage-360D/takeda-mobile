package ru.com.cardiomagnyl.model.pill;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "name",
        "quantity",
        "time",
        "repeat",
        "sinceDate",
        "tillDate"
})
@DatabaseTable(tableName = "pill")
public class Pill {
    public enum Repeat {DAILY, EVERY_2_DAYS}

    @JsonProperty("name")
    private String name;
    @JsonProperty("quantity")
    private int quantity;
    @JsonProperty("time")
    private String time;
    @JsonProperty("repeat")
    private String repeat;
    @JsonProperty("sinceDate")
    private String sinceDate;
    @JsonProperty("tillDate")
    private String tillDate;

    // links
    @DatabaseField(dataType = DataType.STRING, columnName = "user")
    @JsonProperty("user")
    private String user;

    @JsonProperty("links")
    private JsonNode links;

    /**
     * @return The name
     */
    @JsonProperty("name")
    public String getName() {
        return name;
    }

    /**
     * @param name The name
     */
    @JsonProperty("name")
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return The quantity
     */
    @JsonProperty("quantity")
    public int getQuantity() {
        return quantity;
    }

    /**
     * @param quantity The quantity
     */
    @JsonProperty("quantity")
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    /**
     * @return The time
     */
    @JsonProperty("time")
    public String getTime() {
        return time;
    }

    /**
     * @param time The time
     */
    @JsonProperty("time")
    public void setTime(String time) {
        this.time = time;
    }

    /**
     * @return The repeat
     */
    @JsonProperty("repeat")
    public String getRepeat() {
        return repeat;
    }

    /**
     * @param repeat The repeat
     */
    @JsonProperty("repeat")
    public void setRepeat(String repeat) {
        this.repeat = repeat;
    }

    /**
     * @return The sinceDate
     */
    @JsonProperty("sinceDate")
    public String getSinceDate() {
        return sinceDate;
    }

    /**
     * @param sinceDate The sinceDate
     */
    @JsonProperty("sinceDate")
    public void setSinceDate(String sinceDate) {
        this.sinceDate = sinceDate;
    }

    /**
     * @return The tillDate
     */
    @JsonProperty("tillDate")
    public String getTillDate() {
        return tillDate;
    }

    /**
     * @param tillDate The tillDate
     */
    @JsonProperty("tillDate")
    public void setTillDate(String tillDate) {
        this.tillDate = tillDate;
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

    /**
     * @return The region
     */
    @JsonProperty("user")
    public String getUser() {
        return user;
    }

    /**
     * @param user The region
     */
    @JsonProperty("user")
    public void setUser(String user) {
        this.user = user;
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