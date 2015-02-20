package ru.com.cardiomagnyl.model.token;

import com.android.volley.Request;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.support.ConnectionSource;

import java.sql.SQLException;

import ru.com.cardiomagnyl.api.Url;
import ru.com.cardiomagnyl.api.DataLoadDispatcher;
import ru.com.cardiomagnyl.api.db.DbRequestHolder;
import ru.com.cardiomagnyl.api.db.HelperFactory;
import ru.com.cardiomagnyl.api.http.HttpRequestHolder;
import ru.com.cardiomagnyl.model.common.DataWrapper;
import ru.com.cardiomagnyl.model.common.Response;
import ru.com.cardiomagnyl.model.common.LgnPwd;
import ru.com.cardiomagnyl.util.CallbackOne;

public class TokenDao extends BaseDaoImpl<Token, Integer> {
    public TokenDao(ConnectionSource connectionSource, Class<Token> dataClass) throws SQLException {
        super(connectionSource, dataClass);
    }

    public static void getByUserId(final String userId,
                                   final CallbackOne<Token> onSuccess,
                                   final CallbackOne<Response> onFailure) {
        getByIdHelper("user", userId, onSuccess, onFailure);
    }

    public static void getByTokenId(final String tokenId,
                                    final CallbackOne<Token> onSuccess,
                                    final CallbackOne<Response> onFailure) {
        getByIdHelper("token", tokenId, onSuccess, onFailure);
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

        CallbackOne<Response> beforeExtracted = new CallbackOne<Response>() {
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
        String packedToken = DataWrapper.wrap(objectNode).toString();

        HttpRequestHolder httpRequestHolder =
                new HttpRequestHolder
                        .Builder(Request.Method.POST, Url.TOKENS, typeReference)
                        .addHeaders(Url.POST_HEADERS)
                        .setBody(packedToken)
                        .setBeforeExtracted(beforeExtracted)
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
