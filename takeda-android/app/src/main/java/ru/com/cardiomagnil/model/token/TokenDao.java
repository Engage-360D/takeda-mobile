package ru.com.cardiomagnil.model.token;

import com.android.volley.Request;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.support.ConnectionSource;

import java.sql.SQLException;

import ru.com.cardiomagnil.ca_api.Url;
import ru.com.cardiomagnil.ca_api.DataLoadDispatcher;
import ru.com.cardiomagnil.ca_api.db.DbRequestHolder;
import ru.com.cardiomagnil.ca_api.db.HelperFactory;
import ru.com.cardiomagnil.ca_api.http.HttpRequestHolder;
import ru.com.cardiomagnil.model.common.DataWraper;
import ru.com.cardiomagnil.model.common.Response;
import ru.com.cardiomagnil.model.common.LgnPwd;
import ru.com.cardiomagnil.util.CallbackOne;

public class TokenDao extends BaseDaoImpl<Token, Integer> {
    public TokenDao(ConnectionSource connectionSource, Class<Token> dataClass) throws SQLException {
        super(connectionSource, dataClass);
    }

    public static void getByUserId(final String userId,
                                   final CallbackOne<Token> onSuccess,
                                   final CallbackOne<Response> onFailure) {
        getByIdHelper("user_id", userId, onSuccess, onFailure);
    }

    public static void getByTokenId(final String tokenId,
                                    final CallbackOne<Token> onSuccess,
                                    final CallbackOne<Response> onFailure) {
        getByIdHelper("token_id", tokenId, onSuccess, onFailure);
    }

    private static void getByIdHelper(final String idField,
                                      final String idValue,
                                      final CallbackOne<Token> onSuccess,
                                      final CallbackOne<Response> onFailure) {
        RuntimeExceptionDao helperFactoryTokenDao = HelperFactory.getHelper().getRuntimeDataDao(Token.class);
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


    public static void getByLgnPwd(final LgnPwd lgnPwd,
                                   final CallbackOne<Token> onSuccess,
                                   final CallbackOne<Response> onFailure) {
        TypeReference typeReference = new TypeReference<Token>() {
        };

        CallbackOne<Response> onOnBeforeExtract = new CallbackOne<Response>() {
            @Override
            public void execute(Response response) {
                Token.unPackLinks((ObjectNode) response.getData());
            }
        };

        CallbackOne<Token> onStoreIntoDatabase = new CallbackOne<Token>() {
            @Override
            public void execute(Token token) {
                RuntimeExceptionDao helperFactoryToken = HelperFactory.getHelper().getRuntimeDataDao(Token.class);
                helperFactoryToken.createOrUpdate(token);
            }
        };

        ObjectNode objectNode = new ObjectMapper().valueToTree(lgnPwd);
        String packedToken = DataWraper.wrap(objectNode).toString();

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