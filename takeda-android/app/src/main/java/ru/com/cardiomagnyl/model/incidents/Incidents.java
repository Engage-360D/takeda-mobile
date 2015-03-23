package ru.com.cardiomagnyl.model.incidents;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.ArrayList;

import ru.com.cardiomagnyl.model.base.BaseModel;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "hadBypassSurgery",
        "hadHeartAttackOrStroke",
        "hasDiabetes"
})
@DatabaseTable(tableName = "incidents")
public class Incidents extends BaseModel {
    // id <- userId
    @DatabaseField(id = true, canBeNull = false, dataType = DataType.STRING, columnName = "id")
    @JsonProperty("id")
    private String id;

    @DatabaseField(dataType = DataType.BOOLEAN, columnName = "had_bypass_surgery")
    @JsonProperty("hadBypassSurgery")
    private boolean hadBypassSurgery;

    @DatabaseField(dataType = DataType.BOOLEAN, columnName = "had_heart_attack_or_stroke")
    @JsonProperty("hadHeartAttackOrStroke")
    private boolean hadHeartAttackOrStroke;

    @DatabaseField(dataType = DataType.BOOLEAN, columnName = "has_diabetes")
    @JsonProperty("hasDiabetes")
    private boolean hasDiabetes;

    private String comment;

    @DatabaseField(canBeNull = false, dataType = DataType.STRING, columnName = "user_id")
    private String userId;

    /**
     * @return The id
     */
    public String getId() {
        return id;
    }

    /**
     * @param id The id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * @return The hadBypassSurgery
     */
    @JsonProperty("hadBypassSurgery")
    public boolean isHadBypassSurgery() {
        return hadBypassSurgery;
    }

    /**
     * @param hadBypassSurgery The hadBypassSurgery
     */
    @JsonProperty("hadBypassSurgery")
    public void setHadBypassSurgery(boolean hadBypassSurgery) {
        this.hadBypassSurgery = hadBypassSurgery;
    }

    /**
     * @return The hadHeartAttackOrStroke
     */
    @JsonProperty("hadHeartAttackOrStroke")
    public boolean isHadHeartAttackOrStroke() {
        return hadHeartAttackOrStroke;
    }

    /**
     * @param hadHeartAttackOrStroke The hadHeartAttackOrStroke
     */
    @JsonProperty("hadHeartAttackOrStroke")
    public void setHadHeartAttackOrStroke(boolean hadHeartAttackOrStroke) {
        this.hadHeartAttackOrStroke = hadHeartAttackOrStroke;
    }

    /**
     * @return The hasDiabetes
     */
    @JsonProperty("hasDiabetes")
    public boolean isHasDiabetes() {
        return hasDiabetes;
    }

    /**
     * @param hasDiabetes The hasDiabetes
     */
    @JsonProperty("hasDiabetes")
    public void setHasDiabetes(boolean hasDiabetes) {
        this.hasDiabetes = hasDiabetes;
    }

    /**
     * @return The comment
     */
    public String getComment() {
        return comment;
    }

    /**
     * @param comment The comment
     */
    public void setComment(String comment) {
        this.comment = comment;
    }

    /**
     * @return The userId
     */
    public String setUserId() {
        return userId;
    }

    /**
     * @param userId The userId
     */
    public void setUserId(String userId) {
        this.userId = userId;
        this.id = userId;
    }

    @JsonIgnore
    public boolean isEmpty() {
        return !hadBypassSurgery && !hadHeartAttackOrStroke;
    }

    public ObjectNode getObjectNode() {
        ArrayList<String> fieldsToClean = new ArrayList<>();
        fieldsToClean.add("hasDiabetes");
        if (!hadBypassSurgery) fieldsToClean.add("hadBypassSurgery");
        if (!hadHeartAttackOrStroke) fieldsToClean.add("hadHeartAttackOrStroke");

        ObjectNode objectNode = new ObjectMapper().valueToTree(this);

        return objectNode.remove(fieldsToClean);
    }

}