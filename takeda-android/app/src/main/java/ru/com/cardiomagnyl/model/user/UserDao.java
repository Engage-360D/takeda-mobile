package ru.com.cardiomagnyl.model.user;

import com.android.volley.Request;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;
import java.util.List;

import ru.com.cardiomagnyl.api.DataLoadDispatcher;
import ru.com.cardiomagnyl.api.Url;
import ru.com.cardiomagnyl.api.base.BaseRequestHolder;
import ru.com.cardiomagnyl.api.db.DbRequestHolder;
import ru.com.cardiomagnyl.api.db.HelperFactory;
import ru.com.cardiomagnyl.api.http.HttpRequestHolder;
import ru.com.cardiomagnyl.model.common.DataWrapper;
import ru.com.cardiomagnyl.model.common.Dummy;
import ru.com.cardiomagnyl.model.common.Response;
import ru.com.cardiomagnyl.model.incidents.Incidents;
import ru.com.cardiomagnyl.model.pill.Pill;
import ru.com.cardiomagnyl.model.role.UserRole;
import ru.com.cardiomagnyl.model.role.UserRoleDao;
import ru.com.cardiomagnyl.model.task.Task;
import ru.com.cardiomagnyl.model.test.TestPage;
import ru.com.cardiomagnyl.model.test.TestResultHolder;
import ru.com.cardiomagnyl.model.test_diet.TestDiet;
import ru.com.cardiomagnyl.model.test_diet.TestDietResultHolder;
import ru.com.cardiomagnyl.model.test_diet_answer.TestDietAnswer;
import ru.com.cardiomagnyl.model.timeline.Timeline;
import ru.com.cardiomagnyl.model.token.Token;
import ru.com.cardiomagnyl.util.CallbackOne;
import ru.com.cardiomagnyl.util.CallbackOneReturnable;

public class UserDao extends BaseDaoImpl<User, Integer> {
    public enum Source {db, web}

    public UserDao(ConnectionSource connectionSource, Class<User> dataClass) throws SQLException {
        super(connectionSource, dataClass);
    }

    public static void register(final User user,
                                final CallbackOne<User> onSuccess,
                                final CallbackOne<Response> onFailure) {
        TypeReference typeReference = new TypeReference<User>() {
        };

        CallbackOne<Response> beforeExtracted = new CallbackOne<Response>() {
            @Override
            public void execute(Response response) {
                User.unPackLinks((ObjectNode) response.getData());
            }
        };

        CallbackOneReturnable<User, User> afterExtracted = new CallbackOneReturnable<User, User>() {
            @Override
            public User execute(User user) {
                user.setIsDoctor(user.checkRole(User.Roles.role_doctor));
                return user;
            }
        };

        CallbackOne<User> onStoreIntoDatabase = new CallbackOne<User>() {
            @Override
            public void execute(User user) {
                storeIntoDatabase(user);
            }
        };

        ObjectNode objectNode = new ObjectMapper().valueToTree(user);
        User.packLinks(objectNode);
        User.cleanForRegister(objectNode);
        String packedUser = DataWrapper.wrap(objectNode).toString();

        HttpRequestHolder httpRequestHolder =
                new HttpRequestHolder
                        .Builder(Request.Method.POST, Url.USERS, typeReference)
                        .addHeaders(Url.POST_HEADERS)
                        .setBody(packedUser)
                        .setBeforeExtracted(beforeExtracted)
                        .setAfterExtracted(afterExtracted)
                        .setOnStoreIntoDatabase(onStoreIntoDatabase)
                        .create();

        DataLoadDispatcher
                .getInstance()
                .receive(
                        httpRequestHolder,
                        onSuccess,
                        onFailure
                );
    }

    public static void update(final User user,
                              final Token token,
                              final CallbackOne<User> onSuccess,
                              final CallbackOne<Response> onFailure) {
        TypeReference typeReference = new TypeReference<User>() {
        };

        CallbackOne<Response> beforeExtracted = new CallbackOne<Response>() {
            @Override
            public void execute(Response response) {
                User.unPackLinks((ObjectNode) response.getData());
            }
        };

        CallbackOneReturnable<User, User> afterExtracted = new CallbackOneReturnable<User, User>() {
            @Override
            public User execute(User user) {
                user.setIsDoctor(user.checkRole(User.Roles.role_doctor));
                return user;
            }
        };

        CallbackOne<User> onStoreIntoDatabase = new CallbackOne<User>() {
            @Override
            public void execute(User user) {
                storeIntoDatabase(user);
            }
        };

        ObjectNode objectNode = new ObjectMapper().valueToTree(user);
        User.cleanForUpdate(objectNode);
        String cleanedUser = DataWrapper.wrap(objectNode).toString();

        HttpRequestHolder httpRequestHolder =
                new HttpRequestHolder
                        .Builder(Request.Method.PUT, String.format(Url.ACCOUNT_UPDATE, token.getTokenId()), typeReference)
                        .addHeaders(Url.PUT_HEADERS)
                        .setBody(cleanedUser)
                        .setBeforeExtracted(beforeExtracted)
                        .setAfterExtracted(afterExtracted)
                        .setOnStoreIntoDatabase(onStoreIntoDatabase)
                        .create();

        DataLoadDispatcher
                .getInstance()
                .receive(
                        httpRequestHolder,
                        onSuccess,
                        onFailure
                );
    }

    public static void resetPassword(final Email email,
                                     final CallbackOne<List<Dummy>> onSuccess,
                                     final CallbackOne<Response> onFailure) {
        TypeReference typeReference = new TypeReference<List<Dummy>>() {
        };

        ObjectNode objectNode = new ObjectMapper().valueToTree(email);
        String packedEmail = DataWrapper.wrap(objectNode).toString();

        HttpRequestHolder httpRequestHolder =
                new HttpRequestHolder
                        .Builder(Request.Method.POST, Url.ACCOUNT_RESET_PASSWORD, typeReference)
                        .addHeaders(Url.POST_HEADERS)
                        .setBody(packedEmail)
                        .create();

        DataLoadDispatcher
                .getInstance()
                .receive(
                        httpRequestHolder,
                        onSuccess,
                        onFailure
                );
    }

    public static void getByToken(final Token token,
                                  final CallbackOne<User> onSuccess,
                                  final CallbackOne<Response> onFailure,
                                  final Source source) {
        TypeReference typeReference = new TypeReference<User>() {
        };

        CallbackOne<Response> beforeExtracted = new CallbackOne<Response>() {
            @Override
            public void execute(Response response) {
                User.unPackLinks((ObjectNode) response.getData());
            }
        };

        CallbackOneReturnable<User, User> afterExtractedDb = new CallbackOneReturnable<User, User>() {
            @Override
            public User execute(User user) {
                RuntimeExceptionDao helperFactoryUserRole = HelperFactory.getHelper().getRuntimeDataDao(UserRole.class);
                QueryBuilder queryBuilder = helperFactoryUserRole.queryBuilder();
                try {
                    user.setRoles(UserRoleDao.getRolesForUser(user));
                    queryBuilder.where().eq("user", user.getId());
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                user.setIsDoctor(user.checkRole(User.Roles.role_doctor));
                return user;
            }
        };

        CallbackOneReturnable<User, User> afterExtractedHttp = new CallbackOneReturnable<User, User>() {
            @Override
            public User execute(User user) {
                user.setIsDoctor(user.checkRole(User.Roles.role_doctor));
                return user;
            }
        };

        CallbackOne<User> onStoreIntoDatabase = new CallbackOne<User>() {
            @Override
            public void execute(User user) {
                storeIntoDatabase(user);
            }
        };

        RuntimeExceptionDao helperFactoryUserDao = HelperFactory.getHelper().getRuntimeDataDao(User.class);
        QueryBuilder queryBuilder = helperFactoryUserDao.queryBuilder();
        try {
            queryBuilder.where().idEq(token.getUserId());
        } catch (SQLException e) {
            e.printStackTrace();
        }

        DbRequestHolder dbRequestHolder =
                new DbRequestHolder
                        .Builder(queryBuilder)
                        .setQueryMethod(DbRequestHolder.QueryMethod.queryForFirst)
                        .setAfterExtracted(afterExtractedDb)
                        .create();

        HttpRequestHolder httpRequestHolder =
                new HttpRequestHolder
                        .Builder(Request.Method.GET, Url.ACCOUNT, typeReference)
                        .addHeaders(Url.GET_HEADERS)
                        .addParam("token", token.getTokenId())
                        .setBeforeExtracted(beforeExtracted)
                        .setAfterExtracted(afterExtractedHttp)
                        .setOnStoreIntoDatabase(onStoreIntoDatabase)
                        .create();

        BaseRequestHolder requestHolder;
        switch (source) {
            case db:
                requestHolder = dbRequestHolder;
                break;
            default:
                requestHolder = httpRequestHolder;
        }

        DataLoadDispatcher
                .getInstance()
                .receive(
                        requestHolder,
                        onSuccess,
                        onFailure
                );
    }

    public static void resetProfile(final LgnPwd lgnPwd,
                                    final Token token,
                                    final CallbackOne<Dummy> onSuccess,
                                    final CallbackOne<Response> onFailure) {
        TypeReference typeReference = new TypeReference<Dummy>() {
        };

        CallbackOne<Dummy> onStoreIntoDatabase = new CallbackOne<Dummy>() {
            @Override
            public void execute(Dummy dummy) {
                resetProfileDb();
            }
        };

        ObjectNode objectNode = new ObjectMapper().valueToTree(lgnPwd);
        LgnPwd.cleanForReset(objectNode);
        String packedLgnPwd = DataWrapper.wrap(objectNode).toString();

        HttpRequestHolder httpRequestHolder =
                new HttpRequestHolder
                        .Builder(Request.Method.POST, String.format(Url.ACCOUNT_RESET, token.getTokenId()), typeReference)
                        .addHeaders(Url.POST_HEADERS)
                        .setBody(packedLgnPwd)
                        .setOnStoreIntoDatabase(onStoreIntoDatabase)
                        .create();

        DataLoadDispatcher
                .getInstance()
                .receive(
                        httpRequestHolder,
                        onSuccess,
                        onFailure
                );
    }

    // FIXME: clean tables for current user only
    private static void resetProfileDb() {
        RuntimeExceptionDao helperFactoryUser = HelperFactory.getHelper().getRuntimeDataDao(User.class);
        ConnectionSource connectionSource = helperFactoryUser.getConnectionSource();

        try {
            TableUtils.dropTable(connectionSource, TestResultHolder.class, true);
            TableUtils.createTable(connectionSource, TestResultHolder.class);

            TableUtils.dropTable(connectionSource, TestPage.class, true);
            TableUtils.createTable(connectionSource, TestPage.class);

            TableUtils.dropTable(connectionSource, Pill.class, true);
            TableUtils.createTable(connectionSource, Pill.class);

            TableUtils.dropTable(connectionSource, Timeline.class, true);
            TableUtils.createTable(connectionSource, Timeline.class);

            TableUtils.dropTable(connectionSource, Task.class, true);
            TableUtils.createTable(connectionSource, Task.class);

            TableUtils.dropTable(connectionSource, TestDiet.class, true);
            TableUtils.createTable(connectionSource, TestDiet.class);

            TableUtils.dropTable(connectionSource, TestDietAnswer.class, true);
            TableUtils.createTable(connectionSource, TestDietAnswer.class);

            TableUtils.dropTable(connectionSource, TestDietResultHolder.class, true);
            TableUtils.createTable(connectionSource, TestDietResultHolder.class);

            TableUtils.dropTable(connectionSource, Incidents.class, true);
            TableUtils.createTable(connectionSource, Incidents.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void getIsr(final Token token,
                              final CallbackOne<Isr> onSuccess,
                              final CallbackOne<Response> onFailure) {
        TypeReference typeReference = new TypeReference<Dummy>() {
        };

        HttpRequestHolder httpRequestHolder =
                new HttpRequestHolder
                        .Builder(Request.Method.GET, String.format(Url.ACCOUNT_ISR, token.getTokenId()), typeReference)
                        .addHeaders(Url.GET_HEADERS)
                        .create();

        DataLoadDispatcher
                .getInstance()
                .receive(
                        httpRequestHolder,
                        onSuccess,
                        onFailure
                );
    }

    public static void storeIntoDatabase(final User user) {
        final RuntimeExceptionDao helperFactoryUser = HelperFactory.getHelper().getRuntimeDataDao(User.class);
        final UserRoleDao userRoleDao = HelperFactory.getHelper().getUserRoleDao();

        helperFactoryUser.createOrUpdate(user);
        userRoleDao.createOrUpdate(user, user.getRoles());
    }

}

