package ru.com.cardiomagnyl.model.test;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Pair;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import ru.com.cardiomagnyl.model.base.BaseModel;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "state",
        "title",
        "subtitle",
        "text"
})
@DatabaseTable(tableName = "page")
public class TestPage extends BaseModel implements Parcelable {

    @DatabaseField(id = true, canBeNull = false, dataType = DataType.STRING, columnName = "id")
    private String id;

    @DatabaseField(dataType = DataType.STRING, columnName = "state")
    @JsonProperty("state")
    private String state;

    @DatabaseField(dataType = DataType.STRING, columnName = "title")
    @JsonProperty("title")
    private String title;

    @DatabaseField(dataType = DataType.STRING, columnName = "subtitle")
    @JsonProperty("subtitle")
    private String subtitle;

    @DatabaseField(dataType = DataType.STRING, columnName = "text")
    @JsonProperty("text")
    private String text;

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    /**
     * @return The state
     */
    @JsonProperty("state")
    public String getState() {
        return state;
    }

    /**
     * @param state The state
     */
    @JsonProperty("state")
    public void setState(String state) {
        this.state = state;
    }

    /**
     * @return The title
     */
    @JsonProperty("title")
    public String getTitle() {
        return title;
    }

    /**
     * @param title The title
     */
    @JsonProperty("title")
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * @return The subtitle
     */
    @JsonProperty("subtitle")
    public String getSubtitle() {
        return subtitle;
    }

    /**
     * @param subtitle The subtitle
     */
    @JsonProperty("subtitle")
    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    /**
     * @return The text
     */
    @JsonProperty("text")
    public String getText() {
        return text;
    }

    /**
     * @param text The text
     */
    @JsonProperty("text")
    public void setText(String text) {
        this.text = text;
    }

    public static String generatePageId(String link) {
        Pair<String, String> extractIdAndName = extractIdAndName(link);
        return extractIdAndName.first + "_" + extractIdAndName.second;
    }

    public static Pair<String, String> extractIdAndName(String link) {
        String[] separated = link.split("/");
        String testId = separated[separated.length - 2];
        String recomendationName = separated[separated.length - 1];

        return new Pair(testId, recomendationName);
    }

    ///////////////////////////////////////////////////////////////////////
    // implements Parcelable
    ///////////////////////////////////////////////////////////////////////
    public TestPage() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.state);
        dest.writeString(this.title);
        dest.writeString(this.subtitle);
        dest.writeString(this.text);
    }

    private TestPage(Parcel in) {
        this.id = in.readString();
        this.state = in.readString();
        this.title = in.readString();
        this.subtitle = in.readString();
        this.text = in.readString();
    }

    public static final Parcelable.Creator<TestPage> CREATOR = new Parcelable.Creator<TestPage>() {
        public TestPage createFromParcel(Parcel source) {
            return new TestPage(source);
        }

        public TestPage[] newArray(int size) {
            return new TestPage[size];
        }
    };
    ///////////////////////////////////////////////////////////////////////
}