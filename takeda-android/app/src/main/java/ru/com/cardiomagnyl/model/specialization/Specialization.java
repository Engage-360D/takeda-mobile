package ru.com.cardiomagnyl.model.specialization;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import ru.com.cardiomagnyl.app.R;
import ru.com.cardiomagnyl.model.base.BaseModel;
import ru.com.cardiomagnyl.model.base.BaseModelHelper;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "id"
})
@DatabaseTable(tableName = "specialization")
public class Specialization extends BaseModel implements BaseModelHelper<String>, Parcelable {

    @DatabaseField(id = true, canBeNull = false, dataType = DataType.STRING, columnName = "id")
    @JsonProperty("id")
    private String id;

    /**
     * @return The id
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
        return id;
    }

    /**
     * @param name The name
     */
    @JsonProperty("name")
    public void setName(String name) {
        this.id = name;
    }

    public static Specialization createNoSpecialization(Context context) {
        Specialization noSpecialization = new Specialization();
        noSpecialization.setName(context.getString(R.string.select_specialization));
        return noSpecialization;
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
    }

    public Specialization() {
    }

    private Specialization(Parcel in) {
        this.id = in.readString();
    }

    public static final Parcelable.Creator<Specialization> CREATOR = new Parcelable.Creator<Specialization>() {
        public Specialization createFromParcel(Parcel source) {
            return new Specialization(source);
        }

        public Specialization[] newArray(int size) {
            return new Specialization[size];
        }
    };

    ///////////////////////////////////////////////////////////////////////

}