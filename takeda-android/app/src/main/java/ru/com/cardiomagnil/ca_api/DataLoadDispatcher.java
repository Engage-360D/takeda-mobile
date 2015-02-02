package ru.com.cardiomagnil.ca_api;


import ru.com.cardiomagnil.ca_api.base.BaseRequestHolder;
import ru.com.cardiomagnil.model.common.Response;
import ru.com.cardiomagnil.util.Callback;
import ru.com.cardiomagnil.util.CallbackOne;
import ru.com.cardiomagnil.util.ThtreadHelper;

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
        ThtreadHelper.logThread("receiveHelper");
        ThtreadHelper.startNotInMain(new Callback() {
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