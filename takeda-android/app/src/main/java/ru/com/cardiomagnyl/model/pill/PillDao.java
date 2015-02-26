package ru.com.cardiomagnyl.model.pill;

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
import java.util.HashMap;
import java.util.List;

import ru.com.cardiomagnyl.api.DataLoadDispatcher;
import ru.com.cardiomagnyl.api.DataLoadSequence;
import ru.com.cardiomagnyl.api.Url;
import ru.com.cardiomagnyl.api.db.DbRequestHolder;
import ru.com.cardiomagnyl.api.db.HelperFactory;
import ru.com.cardiomagnyl.api.http.HttpRequestHolder;
import ru.com.cardiomagnyl.model.common.DataWrapper;
import ru.com.cardiomagnyl.model.common.Dummy;
import ru.com.cardiomagnyl.model.common.Response;
import ru.com.cardiomagnyl.model.pill_proxy.PillProxy;
import ru.com.cardiomagnyl.model.task.TaskDao;
import ru.com.cardiomagnyl.model.timeline.TimelineDao;
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

    public static void create(final Pill pill,
                              final Token token,
                              final CallbackOne<Pill> onSuccess,
                              final CallbackOne<Response> onFailure) {
        createAndUpdateHelper(
                pill,
                onSuccess,
                onFailure,
                Request.Method.POST,
                String.format(Url.ACCOUNT_PILLS, token.getTokenId()),
                Url.POST_HEADERS);
    }

    public static void update(final Pill pill,
                              final Token token,
                              final CallbackOne<Pill> onSuccess,
                              final CallbackOne<Response> onFailure) {
        createAndUpdateHelper(
                pill,
                onSuccess,
                onFailure,
                Request.Method.PUT,
                String.format(Url.ACCOUNT_PILLS_ID, pill.getId(), token.getTokenId()),
                Url.PUT_HEADERS);
    }

    private static void createAndUpdateHelper(final Pill pill,
                                              final CallbackOne<Pill> onSuccess,
                                              final CallbackOne<Response> onFailure,
                                              final int method,
                                              final String url,
                                              final HashMap<String, String> headers
    ) {
        TypeReference typeReference = new TypeReference<PillProxy>() {
        };

        CallbackOneReturnable<PillProxy, Pill> afterExtracted = new CallbackOneReturnable<PillProxy, Pill>() {
            @Override
            public Pill execute(PillProxy pillsProxy) {
                return pillsProxy.extractPill();
            }
        };

        CallbackOne<Pill> onStoreIntoDatabase = new CallbackOne<Pill>() {
            @Override
            public void execute(Pill pill) {
                storeIntoDatabase(pill);
            }
        };

        ObjectNode objectNode = new ObjectMapper().valueToTree(pill);
        if (method == Request.Method.PUT) Pill.cleanForUpdate(objectNode);
        String wrappedPill = DataWrapper.wrap(objectNode).toString();

        HttpRequestHolder httpRequestHolder =
                new HttpRequestHolder
                        .Builder(method, url, typeReference)
                        .addHeaders(headers)
                        .setBody(wrappedPill)
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

    public static void delete(final Pill pill,
                              final Token token,
                              final CallbackOne<Dummy> onSuccess,
                              final CallbackOne<Response> onFailure) {
        TypeReference typeReference = new TypeReference<Dummy>() {
        };

        CallbackOne<Dummy> onStoreIntoDatabase = new CallbackOne<Dummy>() {
            @Override
            public void execute(Dummy dummy) {
                deleteFromDatabase(pill);
            }
        };

        HttpRequestHolder httpRequestHolder =
                new HttpRequestHolder
                        .Builder(Request.Method.DELETE, String.format(Url.ACCOUNT_PILLS, pill.getId(), token.getTokenId()), typeReference)
                        .addHeaders(Url.DELETE_HEADERS)
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

    public static void storeIntoDatabase(final Pill pill) {
        if (pill != null) {
            // must to clean tables "task" and "timeline"
            TaskDao.clearTable();
            TimelineDao.clearTable();

            RuntimeExceptionDao helperFactoryPill = HelperFactory.getHelper().getRuntimeDataDao(Pill.class);
            helperFactoryPill.createOrUpdate(pill);
        }
    }

    public static void storeIntoDatabase(final List<Pill> pillsList) {
        if (pillsList != null && !pillsList.isEmpty()) {
            // must to clean tables "pill", "task" and "timeline"
            clearTable();
            TaskDao.clearTable();
            TimelineDao.clearTable();

            RuntimeExceptionDao helperFactoryPill = HelperFactory.getHelper().getRuntimeDataDao(Pill.class);
            for (Pill pill : pillsList) {
                helperFactoryPill.createOrUpdate(pill);
            }
        }
    }

    public static void deleteFromDatabase(final Pill pill) {
        if (pill != null) {
            // must to clean tables "task" and "timeline"
            TaskDao.clearTable();
            TimelineDao.clearTable();

            RuntimeExceptionDao helperFactoryPill = HelperFactory.getHelper().getRuntimeDataDao(Pill.class);
            helperFactoryPill.delete(pill);
        }
    }

    public static void clearTable() {
        RuntimeExceptionDao helperFactoryPill = HelperFactory.getHelper().getRuntimeDataDao(Pill.class);
        try {
            TableUtils.clearTable(helperFactoryPill.getConnectionSource(), Pill.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
