package ru.com.cardiomagnil.ca_api.base;

import android.os.Handler;
import android.os.Looper;

import ru.com.cardiomagnil.ca_api.DataLoadDispatcher;
import ru.com.cardiomagnil.ca_api.DataLoadSequence;
import ru.com.cardiomagnil.ca_model.common.Ca_Response;
import ru.com.cardiomagnil.util.CallbackOne;

public abstract class BaseDataLoader {
    public abstract <T> void invokeDataLoad(final DataLoadSequence dataLoadSequence,
                                            final CallbackOne<T> callbackOneOnSuccess,
                                            final CallbackOne<Ca_Response> callbackOneOnError);

    protected <T> void handleResult(final T result,
                                    final Ca_Response error,
                                    final DataLoadSequence dataLoadSequence,
                                    final CallbackOne<T> callbackOneOnSuccess,
                                    final CallbackOne<Ca_Response> callbackOneOnError) {
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
