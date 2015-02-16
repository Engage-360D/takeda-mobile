package ru.com.cardiomagnyl.api;


import ru.com.cardiomagnyl.api.base.BaseRequestHolder;
import ru.com.cardiomagnyl.model.common.Response;
import ru.com.cardiomagnyl.util.Callback;
import ru.com.cardiomagnyl.util.CallbackOne;
import ru.com.cardiomagnyl.util.ThreadHelper;

public class DataLoadDispatcher {
    private static DataLoadDispatcher sDataLoadDispatcher;

    public static synchronized DataLoadDispatcher getInstance() {
        if (sDataLoadDispatcher == null)
            sDataLoadDispatcher = new DataLoadDispatcher();
        return sDataLoadDispatcher;
    }

    private DataLoadDispatcher() {
    }

    public <T> void receive(final BaseRequestHolder requestHolder,
                            final CallbackOne<T> callbackOneOnSuccess,
                            final CallbackOne<Response> callbackOneOnError) {
        DataLoadSequence dataLoadSequence =
                new DataLoadSequence
                        .Builder(requestHolder)
                        .create();
        receiveHelper(dataLoadSequence, callbackOneOnSuccess, callbackOneOnError);
    }

    public <T> void receive(final DataLoadSequence dataLoadSequence,
                            final CallbackOne<T> callbackOneOnSuccess,
                            final CallbackOne<Response> callbackOneOnError) {
        receiveHelper(dataLoadSequence, callbackOneOnSuccess, callbackOneOnError);
    }

    // TODO: each request starts a new thread: limit the number of streams
    public <T> void receiveHelper(final DataLoadSequence dataLoadSequence,
                                  final CallbackOne<T> callbackOneOnSuccess,
                                  final CallbackOne<Response> callbackOneOnError) {
        ThreadHelper.logThread("receiveHelper");
        ThreadHelper.startNotInMain(new Callback() {
            @Override
            public void execute() {
                BaseRequestHolder currentRequestHolder = dataLoadSequence.peek();
                currentRequestHolder
                        .getDataLoader()
                        .invokeDataLoad(dataLoadSequence, callbackOneOnSuccess, callbackOneOnError);
            }
        });
    }
}