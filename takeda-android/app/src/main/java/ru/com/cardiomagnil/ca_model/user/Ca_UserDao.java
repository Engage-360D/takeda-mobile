package ru.com.cardiomagnil.ca_model.user;

import com.android.volley.Request;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.support.ConnectionSource;

import java.sql.SQLException;

import ru.com.cardiomagnil.api.Url;
import ru.com.cardiomagnil.ca_api.DataLoadDispatcher;
import ru.com.cardiomagnil.ca_api.DataLoadSequence;
import ru.com.cardiomagnil.ca_api.db.DbRequestHolder;
import ru.com.cardiomagnil.ca_api.db.HelperFactory;
import ru.com.cardiomagnil.ca_api.http.HttpRequestHolder;
import ru.com.cardiomagnil.ca_model.common.Ca_DataWraper;
import ru.com.cardiomagnil.ca_model.common.Ca_Response;
import ru.com.cardiomagnil.ca_model.region.Ca_Region;
import ru.com.cardiomagnil.ca_model.role.Ca_UserRoleDao;
import ru.com.cardiomagnil.util.CallbackOne;

public class Ca_UserDao extends BaseDaoImpl<Ca_User, Integer> {
    public Ca_UserDao(ConnectionSource connectionSource, Class<Ca_User> dataClass) throws SQLException {
        super(connectionSource, dataClass);
    }

    public static void register(Ca_User user,
                                final CallbackOne<Ca_User> onSuccess,
                                final CallbackOne<Ca_Response> onFailure) {
        TypeReference typeReference = new TypeReference<Ca_User>() {
        };

        CallbackOne<JsonNode> onOnBeforeExtract = new CallbackOne<JsonNode>() {
            @Override
            public void execute(JsonNode jsonNode) {
                Ca_User.unPackLinks((ObjectNode) jsonNode);
            }
        };

        CallbackOne<Ca_User> onStoreIntoDatabase = new CallbackOne<Ca_User>() {
            @Override
            public void execute(Ca_User user) {
                storeIntoDatabase(user);
            }
        };

        ObjectMapper mapper = new ObjectMapper ();
        mapper.configure(SerializationFeature.WRITE_NULL_MAP_VALUES, true);

        ObjectNode objectNode = new ObjectMapper().valueToTree(user);
        Ca_User.packLinks(objectNode);
        Ca_User.caleanReceivedFields(objectNode);
        String packedUser = Ca_DataWraper.wrap(objectNode).toString();

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

    public static void getById(final CallbackOne<Ca_User> onSuccess,
                               final CallbackOne<Ca_Response> onFailure) {
        TypeReference typeReference = new TypeReference<Ca_User>() {
        };

        CallbackOne<Ca_User> onStoreIntoDatabase = new CallbackOne<Ca_User>() {
            @Override
            public void execute(Ca_User user) {
                storeIntoDatabase(user);
            }
        };

        RuntimeExceptionDao helperFactoryRegion = HelperFactory.getHelper().getRuntimeDataDao(Ca_Region.class);
        QueryBuilder queryBuilder = helperFactoryRegion.queryBuilder();

        DbRequestHolder dbRequestHolder =
                new DbRequestHolder
                        .Builder(queryBuilder)
                        .create();

        HttpRequestHolder httpRequestHolder =
                new HttpRequestHolder
                        .Builder(Request.Method.GET, Url.REGIONS, typeReference)
                        .setOnStoreIntoDatabase(onStoreIntoDatabase)
                        .create();


        DataLoadSequence dataLoadSequence =
                new DataLoadSequence
                        .Builder(dbRequestHolder)
                        .addRequestHolder(httpRequestHolder)
                        .create();

        DataLoadDispatcher
                .getInstance()
                .receive(
                        dataLoadSequence,
                        onSuccess,
                        onFailure
                );
    }

    public static void storeIntoDatabase(final Ca_User user) {
        final RuntimeExceptionDao helperFactoryUser = HelperFactory.getHelper().getRuntimeDataDao(Ca_User.class);
        final Ca_UserRoleDao userRoleDao = HelperFactory.getHelper().getUserRoleDao();

        helperFactoryUser.createOrUpdate(user);
        userRoleDao.createOrUpdate(user, user.getRoles());
    }

}
