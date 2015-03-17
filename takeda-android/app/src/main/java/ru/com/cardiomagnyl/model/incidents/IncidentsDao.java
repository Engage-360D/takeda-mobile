package ru.com.cardiomagnyl.model.incidents;

import com.android.volley.Request;
import com.fasterxml.jackson.core.type.TypeReference;
import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.support.ConnectionSource;

import java.sql.SQLException;

import ru.com.cardiomagnyl.api.DataLoadDispatcher;
import ru.com.cardiomagnyl.api.DataLoadSequence;
import ru.com.cardiomagnyl.api.Url;
import ru.com.cardiomagnyl.api.db.DbRequestHolder;
import ru.com.cardiomagnyl.api.db.HelperFactory;
import ru.com.cardiomagnyl.api.http.HttpRequestHolder;
import ru.com.cardiomagnyl.model.common.DataWrapper;
import ru.com.cardiomagnyl.model.common.Dummy;
import ru.com.cardiomagnyl.model.common.Response;
import ru.com.cardiomagnyl.model.token.Token;
import ru.com.cardiomagnyl.util.CallbackOne;

public class IncidentsDao extends BaseDaoImpl<Incidents, Integer> {
    public IncidentsDao(ConnectionSource connectionSource, Class<Incidents> dataClass) throws SQLException {
        super(connectionSource, dataClass);
    }

    public static void getByToken(final Token token,
                                  final CallbackOne<Incidents> onSuccess,
                                  final CallbackOne<Response> onFailure) {
        TypeReference typeReference = new TypeReference<Incidents>() {
        };

        CallbackOne<Incidents> onStoreIntoDatabase = new CallbackOne<Incidents>() {
            @Override
            public void execute(Incidents incidents) {
                storeIntoDatabase(incidents, token);
            }
        };

        RuntimeExceptionDao helperFactoryIncidentsDao = HelperFactory.getHelper().getRuntimeDataDao(Incidents.class);
        QueryBuilder queryBuilder = helperFactoryIncidentsDao.queryBuilder();
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
                        .Builder(Request.Method.GET, String.format(Url.ACCOUNT_INCIDENTS, token.getTokenId()), typeReference)
                        .addHeaders(Url.GET_HEADERS)
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

    public static void report(final Incidents incidents,
                              final Token token,
                              final CallbackOne<Dummy> onSuccess,
                              final CallbackOne<Response> onFailure) {
        TypeReference typeReference = new TypeReference<Dummy>() {
        };

        CallbackOne<Dummy> onStoreIntoDatabase = new CallbackOne<Dummy>() {
            @Override
            public void execute(Dummy dummy) {
                storeIntoDatabase(incidents, token);
            }
        };

        String cleanedIncidents = DataWrapper.wrap(incidents.getObjectNode()).toString();

        HttpRequestHolder httpRequestHolder =
                new HttpRequestHolder
                        .Builder(Request.Method.POST, String.format(Url.ACCOUNT_INCIDENTS, token.getTokenId()), typeReference)
                        .addHeaders(Url.POST_HEADERS)
                        .setBody(cleanedIncidents)
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


    private static void storeIntoDatabase(final Incidents incidents, final Token token) {
        try {
            incidents.setUserId(token.getUserId());
            RuntimeExceptionDao helperFactoryIncidentsDao = HelperFactory.getHelper().getRuntimeDataDao(Incidents.class);
            helperFactoryIncidentsDao.createOrUpdate(incidents);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
