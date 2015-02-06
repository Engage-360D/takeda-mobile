package ru.com.cardiomagnyl.api.base;

import android.os.Handler;
import android.os.Looper;

import ru.com.cardiomagnyl.api.DataLoadDispatcher;
import ru.com.cardiomagnyl.api.DataLoadSequence;
import ru.com.cardiomagnyl.model.common.Response;
import ru.com.cardiomagnyl.util.CallbackOne;

public abstract class BaseDataLoader {
    public abstract <T> void invokeDataLoad(final DataLoadSequence dataLoadSequence,
                                            final CallbackOne<T> callbackOneOnSuccess,
                                            final CallbackOne<Response> callbackOneOnError);

    protected <T> void handleResult(final T result,
                                    final Response error,
                                    final DataLoadSequence dataLoadSequence,
                                    final CallbackOne<T> callbackOneOnSuccess,
                                    final CallbackOne<Response> callbackOneOnError) {
        if (error == null) {
            runOnMain(callbackOneOnSuccess, result);
        } else {
            if (dataLoadSequence.isEmpty()) {
                runOnMain(callbackOneOnError, error);
            } else {
                DataLoadDispatcher.getInstance().receive(dataLoadSequence, callbackOneOnSuccess, callbackOneOnError);
            }
        }
    }

    protected <T> void runOnMain(final CallbackOne<T> callbackOne, final T result) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                callbackOne.execute(result);
            }
        });
    }
}
