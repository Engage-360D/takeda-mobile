package ru.com.cardiomagnyl.model.pill_proxy;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.ArrayList;
import java.util.List;

import ru.com.cardiomagnyl.model.base.BaseModel;
import ru.com.cardiomagnyl.model.pill.Pill;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "id",
        "name",
        "quantity",
        "time",
        "repeat",
        "sinceDate",
        "tillDate",
        "links"
})
public class PillProxy extends BaseModel {

    @JsonProperty("id")
    private String id;

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

    @JsonProperty("links")
    private JsonNode links;

    /**
     * @return The name
     */
    @JsonProperty("id")
    public String getId() {
        return id;
    }

    /**
     * @param id The id
     */
    @JsonProperty("id")
    public void setId(String id) {
        this.id = id;
    }

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

    public static List<Pill> extractAllPills(List<PillProxy> pillProxy) {
        if (pillProxy == null) return null;

        List<Pill> allPills = new ArrayList<>();
        for (PillProxy currentPillProxy : pillProxy) allPills.add(currentPillProxy.extractPill());

        return allPills;
    }

    // FIXME: improve performance!!!
    public Pill extractPill() {
        ObjectNode objectNode = new ObjectMapper().valueToTree(this);
        unPackLinks(objectNode);
        Pill pill = (Pill) BaseModel.stringToObject(objectNode.toString(), new TypeReference<Pill>() {
        });
        return pill;
    }

    public static void unPackLinks(ObjectNode objectNodePacked) {
        if (objectNodePacked != null) {
            JsonNode jsonNode = objectNodePacked.get("links");
            objectNodePacked.remove("links");
            if (jsonNode != null) objectNodePacked.putAll((ObjectNode) jsonNode);
        }
    }

}