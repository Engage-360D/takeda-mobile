package ru.com.cardiomagnil.ca_model.role;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import ru.com.cardiomagnil.ca_model.base.BaseModel;
import ru.com.cardiomagnil.ca_model.user.Ca_User;
import ru.com.cardiomagnil.util.Utils;

@DatabaseTable(tableName = "user_role")
public class Ca_UserRole extends BaseModel {
    @DatabaseField(id = true, canBeNull = false, dataType = DataType.STRING, columnName = "id")
    private String id;

    @DatabaseField(foreign = true, columnName = "user_id")
    private Ca_User user;

    @DatabaseField(foreign = true, columnName = "role_id")
    private Ca_Role role;

    public Ca_UserRole() {
    }

    public Ca_UserRole(Ca_User user, Ca_Role role) {
        this.user = user;
        this.role = role;
        this.id = Utils.md5(String.valueOf(user.getId()) + String.valueOf(role.getId()));
    }

}
