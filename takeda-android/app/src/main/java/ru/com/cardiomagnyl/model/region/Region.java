package ru.com.cardiomagnyl.model.region;

import android.content.Context;

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
        "id",
        "name"
})
@DatabaseTable(tableName = "region")
public class Region extends BaseModel implements BaseModelHelper<Integer> {

    @DatabaseField(id = true, canBeNull = false, dataType = DataType.INTEGER, columnName = "id")
    @JsonProperty("id")
    private int id;

    @DatabaseField(dataType = DataType.STRING, columnName = "name")
    @JsonProperty("name")
    private String name;

    /**
     * @return The id
     */
    @JsonProperty("id")
    public Integer getId() {
        return id;
    }

    /**
     * @param id The id
     */
    @JsonProperty("id")
    public void setId(Integer id) {
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

    public static Region createNoRegion(Context context) {
        Region noRegion = new Region();
        noRegion.setId(-1);
        noRegion.setName(context.getString(R.string.not_specified));
        return noRegion;
    }

}