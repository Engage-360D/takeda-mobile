package ru.com.cardiomagnyl.model.pill;

import android.os.Parcel;
import android.os.Parcelable;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ru.com.cardiomagnyl.model.base.BaseModel;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "id",
        "name",
        "quantity",
        "time",
        "repeat",
        "sinceDate",
        "tillDate"
})
@DatabaseTable(tableName = "pill")
public class Pill extends BaseModel implements Parcelable {

    @DatabaseField(id = true, canBeNull = false, dataType = DataType.STRING, columnName = "id")
    @JsonProperty("id")
    private String id;

    @DatabaseField(dataType = DataType.STRING, columnName = "name")
    @JsonProperty("name")
    private String name;

    @DatabaseField(dataType = DataType.INTEGER, columnName = "quantity")
    @JsonProperty("quantity")
    private int quantity;

    @DatabaseField(dataType = DataType.STRING, columnName = "time")
    @JsonProperty("time")
    private String time;

    @DatabaseField(dataType = DataType.STRING, columnName = "repeat")
    @JsonProperty("repeat")
    private String repeat;

    @DatabaseField(dataType = DataType.STRING, columnName = "since_date")
    @JsonProperty("sinceDate")
    private String sinceDate;

    @DatabaseField(dataType = DataType.STRING, columnName = "till_date")
    @JsonProperty("tillDate")
    private String tillDate;

    // links
    @DatabaseField(dataType = DataType.STRING, columnName = "user")
    @JsonProperty("user")
    private String user;

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

    public static Map<String, Pill> listToMap(List<Pill> pillsList) {
        if (pillsList == null) return null;

        Map<String, Pill> pillsMap = new HashMap<>();
        for (Pill pill : pillsList) pillsMap.put(pill.getId(), pill);

        return pillsMap;
    }

    public PillFrequency getEnumFrequency() {
        PillFrequency enumFrequency = PillFrequency.undefined;
        try {
            enumFrequency = PillFrequency.valueOf(repeat.toLowerCase());
        } catch (Exception ex) { /*do nothing */ }
        return enumFrequency;
    }

    public static void cleanForUpdate(ObjectNode objectNodeUncleaned) {
        JsonNode jsonNodeRepeat = objectNodeUncleaned.get("repeat");
        String repeat = jsonNodeRepeat.asText().toUpperCase();

        objectNodeUncleaned.remove(Arrays
                .asList("id",
                        "repeat",
                        "links"));
        objectNodeUncleaned.put("repeat", repeat);
    }

    ///////////////////////////////////////////////////////////////////////
    // implements Parcelable
    ///////////////////////////////////////////////////////////////////////

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.name);
        dest.writeInt(this.quantity);
        dest.writeString(this.time);
        dest.writeString(this.repeat);
        dest.writeString(this.sinceDate);
        dest.writeString(this.tillDate);
        dest.writeString(this.user);
    }

    public Pill() {
    }

    private Pill(Parcel in) {
        this.id = in.readString();
        this.name = in.readString();
        this.quantity = in.readInt();
        this.time = in.readString();
        this.repeat = in.readString();
        this.sinceDate = in.readString();
        this.tillDate = in.readString();
        this.user = in.readString();
    }

    public static final Parcelable.Creator<Pill> CREATOR = new Parcelable.Creator<Pill>() {
        public Pill createFromParcel(Parcel source) {
            return new Pill(source);
        }

        public Pill[] newArray(int size) {
            return new Pill[size];
        }
    };

    ///////////////////////////////////////////////////////////////////////

}