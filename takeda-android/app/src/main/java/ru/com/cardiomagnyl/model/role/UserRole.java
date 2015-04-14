package ru.com.cardiomagnyl.model.role;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import ru.com.cardiomagnyl.model.base.BaseModel;
import ru.com.cardiomagnyl.model.user.User;
import ru.com.cardiomagnyl.util.Tools;

@DatabaseTable(tableName = "user_role")
public class UserRole extends BaseModel {
    @DatabaseField(id = true, canBeNull = false, dataType = DataType.STRING, columnName = "id")
    private String id;

    @DatabaseField(canBeNull = false, foreign = true, columnName = "user")
    private User user;

    @DatabaseField(canBeNull = false, foreign = true, columnName = "role")
    private Role role;

    public UserRole() {
    }

    public UserRole(User user, Role role) {
        this.user = user;
        this.role = role;
        this.id = Tools.md5(String.valueOf(user.getId()) + String.valueOf(role.getId()));
    }

}
