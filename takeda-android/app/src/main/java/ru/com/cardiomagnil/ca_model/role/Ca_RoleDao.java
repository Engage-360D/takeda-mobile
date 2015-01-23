package ru.com.cardiomagnil.ca_model.role;

import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.support.ConnectionSource;

import java.sql.SQLException;
import java.util.List;

public class Ca_RoleDao extends BaseDaoImpl<Ca_Role, Integer> {
    public Ca_RoleDao(ConnectionSource connectionSource, Class<Ca_Role> dataClass) throws SQLException {
        super(connectionSource, dataClass);
    }

    public void createOrUpdate(List<String> roles) throws SQLException {
        if (roles != null && !roles.isEmpty()) {
            for (String role : roles) {
                createOrUpdate(new Ca_Role(role, role));
            }
        }
    }
}
