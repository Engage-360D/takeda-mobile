package ru.com.cardiomagnil.ca_model.role;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import ru.com.cardiomagnil.ca_model.base.BaseModel;

@DatabaseTable(tableName = "role")
public class Ca_Role extends BaseModel {

    @DatabaseField(id = true, canBeNull = false, dataType = DataType.STRING, columnName = "id")
    private String id;

    @DatabaseField(dataType = DataType.STRING, unique=true, columnName = "name")
    private String name;

    public Ca_Role() {
    }

    public Ca_Role(String id, String name) {
        this.id = id;
        this.name = name;
    }

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
     * @return The name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name The name
     */
    public void setName(String name) {
        this.name = name;
    }

}