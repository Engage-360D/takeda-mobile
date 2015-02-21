package ru.com.cardiomagnyl.model.task;

import com.android.volley.Request;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.support.ConnectionSource;

import java.sql.SQLException;

import ru.com.cardiomagnyl.api.DataLoadDispatcher;
import ru.com.cardiomagnyl.api.Url;
import ru.com.cardiomagnyl.api.db.HelperFactory;
import ru.com.cardiomagnyl.api.http.HttpRequestHolder;
import ru.com.cardiomagnyl.model.common.DataWrapper;
import ru.com.cardiomagnyl.model.common.Response;
import ru.com.cardiomagnyl.model.task_proxy.TaskProxy;
import ru.com.cardiomagnyl.model.token.Token;
import ru.com.cardiomagnyl.util.CallbackOne;
import ru.com.cardiomagnyl.util.CallbackOneReturnable;

public class TaskDao extends BaseDaoImpl<Task, Integer> {
    public TaskDao(ConnectionSource connectionSource, Class<Task> dataClass) throws SQLException {
        super(connectionSource, dataClass);
    }

    public static void update(final Task task,
                              final ObjectNode taskResult,
                              final Token token,
                              final CallbackOne<Task> onSuccess,
                              final CallbackOne<Response> onFailure) {
        TypeReference typeReference = new TypeReference<TaskProxy>() {
        };

        CallbackOneReturnable<TaskProxy, Task> afterExtracted = new CallbackOneReturnable<TaskProxy, Task>() {
            @Override
            public Task execute(TaskProxy taskProxy) {
                return taskProxy.extractTask();
            }
        };

        CallbackOne<Task> onStoreIntoDatabase = new CallbackOne<Task>() {
            @Override
            public void execute(Task task) {
                storeIntoDatabase(task);
            }
        };

        String wrappedTaskResult = DataWrapper.wrap(taskResult).toString();

        HttpRequestHolder httpRequestHolder =
                new HttpRequestHolder
                        .Builder(Request.Method.PUT, String.format(Url.ACCOUNT_TIMELINE_TASKS, task.getId(), token.getTokenId()), typeReference)
                        .addHeaders(Url.PUT_HEADERS)
                        .setBody(wrappedTaskResult)
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

    public static void storeIntoDatabase(final Task task) {
        final RuntimeExceptionDao helperFactoryTask = HelperFactory.getHelper().getRuntimeDataDao(Task.class);

        helperFactoryTask.createOrUpdate(task);
    }

}

