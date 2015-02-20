package ru.com.cardiomagnyl.model.pill;

import com.android.volley.Request;
import com.fasterxml.jackson.core.type.TypeReference;
import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.support.ConnectionSource;

import java.sql.SQLException;
import java.util.List;

import ru.com.cardiomagnyl.api.DataLoadDispatcher;
import ru.com.cardiomagnyl.api.DataLoadSequence;
import ru.com.cardiomagnyl.api.Url;
import ru.com.cardiomagnyl.api.db.DbRequestHolder;
import ru.com.cardiomagnyl.api.db.HelperFactory;
import ru.com.cardiomagnyl.api.http.HttpRequestHolder;
import ru.com.cardiomagnyl.model.common.Response;
import ru.com.cardiomagnyl.model.pill_proxy.PillProxy;
import ru.com.cardiomagnyl.model.token.Token;
import ru.com.cardiomagnyl.util.CallbackOne;
import ru.com.cardiomagnyl.util.CallbackOneReturnable;

public class PillDao extends BaseDaoImpl<Pill, Integer> {
    public enum Source {http, database, http_database, database_http}

    public PillDao(ConnectionSource connectionSource, Class<Pill> dataClass) throws SQLException {
        super(connectionSource, dataClass);
    }

    public static void getAll(final Token token,
                              final CallbackOne<List<Pill>> onSuccess,
                              final CallbackOne<Response> onFailure,
                              final Source source) {
        TypeReference typeReference = new TypeReference<List<PillProxy>>() {
        };

        CallbackOneReturnable<List<PillProxy>, List<Pill>> afterExtracted = new CallbackOneReturnable<List<PillProxy>, List<Pill>>() {
            @Override
            public List<Pill> execute(List<PillProxy> pillsProxyList) {
                return pillsProxyList != null ? PillProxy.extractAllPills(pillsProxyList) : null;
            }
        };

        CallbackOne<List<Pill>> onStoreIntoDatabase = new CallbackOne<List<Pill>>() {
            @Override
            public void execute(List<Pill> pillsList) {
                storeIntoDatabase(pillsList);
            }
        };

        RuntimeExceptionDao helperFactoryPill = HelperFactory.getHelper().getRuntimeDataDao(Pill.class);
        QueryBuilder queryBuilder = helperFactoryPill.queryBuilder();
        try {
            queryBuilder.where().eq("user", token.getUserId());
        } catch (SQLException e) {
            e.printStackTrace();
        }

        DbRequestHolder dbRequestHolder =
                new DbRequestHolder
                        .Builder(queryBuilder)
                        .create();

        HttpRequestHolder httpRequestHolder =
                new HttpRequestHolder
                        .Builder(Request.Method.GET, String.format(Url.ACCOUNT_PILLS, token.getTokenId()), typeReference)
                        .addHeaders(Url.GET_HEADERS)
                        .setAfterExtracted(afterExtracted)
                        .setOnStoreIntoDatabase(onStoreIntoDatabase)
                        .create();

        DataLoadSequence dataLoadSequence;
        switch (source) {
            case http:
                dataLoadSequence = new DataLoadSequence
                        .Builder(httpRequestHolder)
                        .create();
                break;
            case database:
                dataLoadSequence = new DataLoadSequence
                        .Builder(dbRequestHolder)
                        .create();
                break;
            case http_database:
                dataLoadSequence = new DataLoadSequence
                        .Builder(httpRequestHolder)
                        .addRequestHolder(dbRequestHolder)
                        .create();
                break;
            default:
                // the same is for database_http
                dataLoadSequence = new DataLoadSequence
                        .Builder(dbRequestHolder)
                        .addRequestHolder(httpRequestHolder)
                        .create();
                break;
        }

        DataLoadDispatcher
                .getInstance()
                .receive(
                        dataLoadSequence,
                        onSuccess,
                        onFailure
                );
    }

    public static void storeIntoDatabase(final List<Pill> pillsList) {
        if (pillsList != null && !pillsList.isEmpty()) {
            RuntimeExceptionDao helperFactoryPill = HelperFactory.getHelper().getRuntimeDataDao(Pill.class);
            for (Pill pill : pillsList) {
                helperFactoryPill.createOrUpdate(pill);
            }
        }
    }

}
