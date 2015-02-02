package ru.com.cardiomagnil.model.role;

import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.support.ConnectionSource;

import java.sql.SQLException;
import java.util.List;

public class RoleDao extends BaseDaoImpl<Role, Integer> {
    public RoleDao(ConnectionSource connectionSource, Class<Role> dataClass) throws SQLException {
        super(connectionSource, dataClass);
    }

    public void createOrUpdate(List<String> roles) throws SQLException {
        if (roles != null && !roles.isEmpty()) {
            for (String role : roles) {
                createOrUpdate(new Role(role, role));
            }
        }
    }
}
