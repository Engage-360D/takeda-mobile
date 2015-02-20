package ru.com.cardiomagnyl.model.timeline;

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
import ru.com.cardiomagnyl.model.task.Task;
import ru.com.cardiomagnyl.model.timeline_proxy.TimelineMergedProxy;
import ru.com.cardiomagnyl.model.token.Token;
import ru.com.cardiomagnyl.util.CallbackOne;
import ru.com.cardiomagnyl.util.CallbackOneReturnable;

public class TimelineDao extends BaseDaoImpl<Timeline, Integer> {
    public TimelineDao(ConnectionSource connectionSource, Class<Timeline> dataClass) throws SQLException {
        super(connectionSource, dataClass);
    }

    public static void getAll(final Token token,
                              final CallbackOne<List<Timeline>> onSuccess,
                              final CallbackOne<Response> onFailure) {
        TypeReference typeReference = new TypeReference<TimelineMergedProxy>() {
        };

        CallbackOne<Response> beforeExtracted = new CallbackOne<Response>() {
            @Override
            public void execute(Response response) {
                TimelineMergedProxy.mergeResponse(response);
            }
        };

        CallbackOneReturnable<TimelineMergedProxy, List<Timeline>> afterExtracted = new CallbackOneReturnable<TimelineMergedProxy, List<Timeline>>() {
            @Override
            public List<Timeline> execute(TimelineMergedProxy timelineMergedProxy) {
                return timelineMergedProxy != null ? timelineMergedProxy.extractAllTimeline() : null;
            }
        };

        CallbackOne<List<Timeline>> onStoreIntoDatabase = new CallbackOne<List<Timeline>>() {
            @Override
            public void execute(List<Timeline> timeline) {
                storeIntoDatabase(timeline, token.getUserId());
            }
        };

        RuntimeExceptionDao helperFactoryTimeline = HelperFactory.getHelper().getRuntimeDataDao(Timeline.class);
        QueryBuilder queryBuilder = helperFactoryTimeline.queryBuilder();
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
                        .Builder(Request.Method.GET, String.format(Url.ACCOUNT_TIMELINE, token.getTokenId()), typeReference)
                        .addHeaders(Url.GET_HEADERS)
                        .setBeforeExtracted(beforeExtracted)
                        .setAfterExtracted(afterExtracted)
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

    public static void storeIntoDatabase(final List<Timeline> timeline, final String userId) {
        if (timeline != null && !timeline.isEmpty()) {
            final RuntimeExceptionDao helperFactoryTimeline = HelperFactory.getHelper().getRuntimeDataDao(Timeline.class);
            final RuntimeExceptionDao helperFactoryTask = HelperFactory.getHelper().getRuntimeDataDao(Task.class);

            for (Timeline currentTimeline : timeline) {
                currentTimeline.setUserId(userId);
                helperFactoryTimeline.createOrUpdate(currentTimeline);
                for (Task currentTask : currentTimeline.getTasks()) {
                    currentTask.setTimeline(currentTimeline);
                    helperFactoryTask.createOrUpdate(currentTask);
                }
            }
        }
    }

}
