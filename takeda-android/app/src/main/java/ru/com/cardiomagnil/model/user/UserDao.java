package ru.com.cardiomagnil.model.user;

import com.android.volley.Request;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.support.ConnectionSource;

import java.sql.SQLException;

import ru.com.cardiomagnil.ca_api.DataLoadDispatcher;
import ru.com.cardiomagnil.ca_api.DataLoadSequence;
import ru.com.cardiomagnil.ca_api.Url;
import ru.com.cardiomagnil.ca_api.db.DbRequestHolder;
import ru.com.cardiomagnil.ca_api.db.HelperFactory;
import ru.com.cardiomagnil.ca_api.http.HttpRequestHolder;
import ru.com.cardiomagnil.model.common.DataWraper;
import ru.com.cardiomagnil.model.common.Response;
import ru.com.cardiomagnil.model.role.UserRoleDao;
import ru.com.cardiomagnil.model.token.Token;
import ru.com.cardiomagnil.util.CallbackOne;

public class UserDao extends BaseDaoImpl<User, Integer> {
    public UserDao(ConnectionSource connectionSource, Class<User> dataClass) throws SQLException {
        super(connectionSource, dataClass);
    }

    public static void register(final User user,
                                final CallbackOne<User> onSuccess,
                                final CallbackOne<Response> onFailure) {
        TypeReference typeReference = new TypeReference<User>() {
        };

        CallbackOne<Response> onOnBeforeExtract = new CallbackOne<Response>() {
            @Override
            public void execute(Response response) {
                User.unPackLinks((ObjectNode) response.getData());
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
        User.cleanReceivedFields(objectNode);
        String packedUser = DataWraper.wrap(objectNode).toString();

        HttpRequestHolder httpRequestHolder =
                new HttpRequestHolder
                        .Builder(Request.Method.POST, Url.USERS, typeReference)
                        .addHeaders(Url.POST_HEADERS)
                        .setBody(packedUser)
                        .setOnBeforeExtract(onOnBeforeExtract)
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

    // FIXME!!!
    public static void resetPassword(final String email,
                                     final CallbackOne<User> onSuccess,
                                     final CallbackOne<Response> onFailure) {
        TypeReference typeReference = new TypeReference<User>() {
        };

        CallbackOne<Response> onOnBeforeExtract = new CallbackOne<Response>() {
            @Override
            public void execute(Response response) {
                User.unPackLinks((ObjectNode) response.getData());
            }
        };

        HttpRequestHolder httpRequestHolder =
                new HttpRequestHolder
                        .Builder(Request.Method.POST, Url.ACCOUNT_RESET_PASSWORD, typeReference)
                        .addParam("email", email)
                        .setOnBeforeExtract(onOnBeforeExtract)
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
                                  final boolean forceHttp) {
        TypeReference typeReference = new TypeReference<User>() {
        };

        CallbackOne<Response> onOnBeforeExtract = new CallbackOne<Response>() {
            @Override
            public void execute(Response response) {
                User.unPackLinks((ObjectNode) response.getData());
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
                        .create();

        HttpRequestHolder httpRequestHolder =
                new HttpRequestHolder
                        .Builder(Request.Method.GET, Url.ACCOUNT, typeReference)
                        .addParam("token", token.getTokenId())
                        .setOnBeforeExtract(onOnBeforeExtract)
                        .setOnStoreIntoDatabase(onStoreIntoDatabase)
                        .create();

        DataLoadSequence dataLoadSequence =
                new DataLoadSequence
                        .Builder(forceHttp ? httpRequestHolder : dbRequestHolder)
                        .addRequestHolder(forceHttp ? dbRequestHolder : httpRequestHolder)
                        .create();

        DataLoadDispatcher
                .getInstance()
                .receive(
                        dataLoadSequence,
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
