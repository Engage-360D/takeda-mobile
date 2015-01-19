package ru.com.cardiomagnil.ca_model.token;

import com.android.volley.Request;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.support.ConnectionSource;

import java.sql.SQLException;

import ru.com.cardiomagnil.api.Url;
import ru.com.cardiomagnil.ca_api.DataLoadDispatcher;
import ru.com.cardiomagnil.ca_api.db.DbRequestHolder;
import ru.com.cardiomagnil.ca_api.db.HelperFactory;
import ru.com.cardiomagnil.ca_api.http.HttpRequestHolder;
import ru.com.cardiomagnil.ca_model.common.Ca_DataWraper;
import ru.com.cardiomagnil.ca_model.common.Ca_Response;
import ru.com.cardiomagnil.ca_model.user.Ca_UserLgnPwd;
import ru.com.cardiomagnil.util.CallbackOne;

public class Ca_TokenDao extends BaseDaoImpl<Ca_Token, Integer> {
    public Ca_TokenDao(ConnectionSource connectionSource, Class<Ca_Token> dataClass) throws SQLException {
        super(connectionSource, dataClass);
    }

    public static void getByUserId(final String userId,
                                   final CallbackOne<Ca_Token> onSuccess,
                                   final CallbackOne<Ca_Response> onFailure) {
        getByIdHelper("user_id", userId, onSuccess, onFailure);
    }

    public static void getByTokenId(final String tokenId,
                                    final CallbackOne<Ca_Token> onSuccess,
                                    final CallbackOne<Ca_Response> onFailure) {
        getByIdHelper("token_id", tokenId, onSuccess, onFailure);
    }

    private static void getByIdHelper(final String idField,
                                      final String idValue,
                                      final CallbackOne<Ca_Token> onSuccess,
                                      final CallbackOne<Ca_Response> onFailure) {
        RuntimeExceptionDao helperFactoryTokenDao = HelperFactory.getHelper().getRuntimeDataDao(Ca_Token.class);
        QueryBuilder queryBuilder = helperFactoryTokenDao.queryBuilder();
        try {
            queryBuilder.where().eq(idField, idValue);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        DbRequestHolder dbRequestHolder =
                new DbRequestHolder
                        .Builder(queryBuilder)
                        .setQueryMethod(DbRequestHolder.QueryMethod.queryForFirst)
                        .create();

        DataLoadDispatcher
                .getInstance()
                .receive(
                        dbRequestHolder,
                        onSuccess,
                        onFailure
                );
    }


    public static void getByLgnPwd(final Ca_UserLgnPwd userLgnPwd,
                                   final CallbackOne<Ca_Token> onSuccess,
                                   final CallbackOne<Ca_Response> onFailure) {
        TypeReference typeReference = new TypeReference<Ca_Token>() {
        };

        CallbackOne<Ca_Response> onOnBeforeExtract = new CallbackOne<Ca_Response>() {
            @Override
            public void execute(Ca_Response response) {
                Ca_Token.unPackLinks((ObjectNode) response.getData());
            }
        };

        CallbackOne<Ca_Token> onStoreIntoDatabase = new CallbackOne<Ca_Token>() {
            @Override
            public void execute(Ca_Token token) {
                RuntimeExceptionDao helperFactoryToken = HelperFactory.getHelper().getRuntimeDataDao(Ca_Token.class);
                helperFactoryToken.createOrUpdate(token);
            }
        };

        ObjectNode objectNode = new ObjectMapper().valueToTree(userLgnPwd);
        String packedToken = Ca_DataWraper.wrap(objectNode).toString();

        HttpRequestHolder httpRequestHolder =
                new HttpRequestHolder
                        .Builder(Request.Method.POST, Url.TOKENS, typeReference)
                        .addHeaders(Url.POST_HEADERS)
                        .setBody(packedToken)
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

}
