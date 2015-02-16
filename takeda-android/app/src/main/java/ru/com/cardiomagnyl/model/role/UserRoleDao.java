package ru.com.cardiomagnyl.model.role;

import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.SelectArg;
import com.j256.ormlite.support.ConnectionSource;

import java.sql.SQLException;
import java.util.List;

import ru.com.cardiomagnyl.api.db.HelperFactory;
import ru.com.cardiomagnyl.model.user.User;
import ru.com.cardiomagnyl.model.user.UserDao;

public class UserRoleDao extends BaseDaoImpl<UserRole, Integer> {
    public UserRoleDao(ConnectionSource connectionSource, Class<UserRole> dataClass) throws SQLException {
        super(connectionSource, dataClass);
    }

    public static void createOrUpdate(User user, List<String> roles) {
        try {
            if (user != null && roles != null && !roles.isEmpty()) {
                UserDao userDao = HelperFactory.getHelper().getUserDao();
                RoleDao roleDao = HelperFactory.getHelper().getRoleDao();
                UserRoleDao userRoleDao = HelperFactory.getHelper().getUserRoleDao();

                userDao.createOrUpdate(user);
                for (String roleAsString : roles) {
                    Role role = new Role(roleAsString, roleAsString);
                    roleDao.createOrUpdate(role);
                    UserRole userRole = new UserRole(user, role);
                    userRoleDao.createOrUpdate(userRole);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public static List<Role> getRolesForUser(User user) throws SQLException {
        UserRoleDao userRoleDao = HelperFactory.getHelper().getUserRoleDao();
        RoleDao roleDao = HelperFactory.getHelper().getRoleDao();

        QueryBuilder<UserRole, Integer> userRoleQb = userRoleDao.queryBuilder();
        // TODO: move to public final string
        userRoleQb.selectColumns("role_id");
        SelectArg userSelectArg = new SelectArg();
        // TODO: move to public final string
        userRoleQb.where().eq("user_id", userSelectArg);

        QueryBuilder<Role, Integer> roleQb = roleDao.queryBuilder();
        // TODO: move to public final string
        roleQb.where().in("id", userRoleQb);
        PreparedQuery<Role> rolesForUser = roleQb.prepare();

        rolesForUser.setArgumentHolderValue(0, user);
        return roleDao.query(rolesForUser);
    }

    public static List<User> getUsersForRole(Role role) throws SQLException {
        UserRoleDao userRoleDao = HelperFactory.getHelper().getUserRoleDao();
        UserDao userDao = HelperFactory.getHelper().getUserDao();

        QueryBuilder<UserRole, Integer> userRoleQb = userRoleDao.queryBuilder();
        // TODO: move to public final string
        userRoleQb.selectColumns("user_id");
        SelectArg roleSelectArg = new SelectArg();
        // TODO: move to public final string
        userRoleQb.where().eq("role_id", roleSelectArg);

        QueryBuilder<User, Integer> userQb = userDao.queryBuilder();
        // TODO: move to public final string
        userQb.where().in("id", userRoleQb);
        PreparedQuery<User> usersForRole = userQb.prepare();

        usersForRole.setArgumentHolderValue(0, role);
        return userDao.query(usersForRole);
    }
}
