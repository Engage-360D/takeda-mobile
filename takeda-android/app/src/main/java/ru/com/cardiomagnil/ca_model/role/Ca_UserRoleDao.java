package ru.com.cardiomagnil.ca_model.role;

import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.SelectArg;
import com.j256.ormlite.support.ConnectionSource;

import java.sql.SQLException;
import java.util.List;

import ru.com.cardiomagnil.ca_api.db.HelperFactory;
import ru.com.cardiomagnil.ca_model.user.Ca_User;
import ru.com.cardiomagnil.ca_model.user.Ca_UserDao;

public class Ca_UserRoleDao extends BaseDaoImpl<Ca_UserRole, Integer> {
    public Ca_UserRoleDao(ConnectionSource connectionSource, Class<Ca_UserRole> dataClass) throws SQLException {
        super(connectionSource, dataClass);
    }

    public void createOrUpdate(Ca_User user, List<String> roles) {
        try {
            if (user != null && roles != null && !roles.isEmpty()) {
                Ca_UserDao userDao = HelperFactory.getHelper().getUserDao();
                Ca_RoleDao roleDao = HelperFactory.getHelper().getRoleDao();
                Ca_UserRoleDao userRoleDao = HelperFactory.getHelper().getUserRoleDao();

                userDao.createOrUpdate(user);
                for (String roleAsString : roles) {
                    Ca_Role role = new Ca_Role(roleAsString, roleAsString);
                    roleDao.createOrUpdate(role);
                    Ca_UserRole userRole = new Ca_UserRole(user, role);
                    userRoleDao.createOrUpdate(userRole);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public List<Ca_Role> getRolesForUser(Ca_User user) throws SQLException {
        Ca_UserRoleDao userRoleDao = HelperFactory.getHelper().getUserRoleDao();
        Ca_RoleDao roleDao = HelperFactory.getHelper().getRoleDao();

        QueryBuilder<Ca_UserRole, Integer> userRoleQb = userRoleDao.queryBuilder();
        // TODO: move to public final string
        userRoleQb.selectColumns("role_id");
        SelectArg userSelectArg = new SelectArg();
        // TODO: move to public final string
        userRoleQb.where().eq("user_id", userSelectArg);

        QueryBuilder<Ca_Role, Integer> roleQb = roleDao.queryBuilder();
        // TODO: move to public final string
        roleQb.where().in("id", userRoleQb);
        PreparedQuery<Ca_Role> rolesForUser = roleQb.prepare();

        rolesForUser.setArgumentHolderValue(0, user);
        return roleDao.query(rolesForUser);
    }

    public List<Ca_User> getUsersForRole(Ca_Role role) throws SQLException {
        Ca_UserRoleDao userRoleDao = HelperFactory.getHelper().getUserRoleDao();
        Ca_UserDao userDao = HelperFactory.getHelper().getUserDao();

        QueryBuilder<Ca_UserRole, Integer> userRoleQb = userRoleDao.queryBuilder();
        // TODO: move to public final string
        userRoleQb.selectColumns("user_id");
        SelectArg roleSelectArg = new SelectArg();
        // TODO: move to public final string
        userRoleQb.where().eq("role_id", roleSelectArg);

        QueryBuilder<Ca_User, Integer> userQb = userDao.queryBuilder();
        // TODO: move to public final string
        userQb.where().in("id", userRoleQb);
        PreparedQuery<Ca_User> usersForRole = userQb.prepare();

        usersForRole.setArgumentHolderValue(0, role);
        return userDao.query(usersForRole);
    }
}
