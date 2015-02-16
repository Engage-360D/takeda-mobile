package ru.com.cardiomagnyl.api.base;


import com.fasterxml.jackson.core.type.TypeReference;

import ru.com.cardiomagnyl.api.DataLoadSequence;
import ru.com.cardiomagnyl.api.Status;
import ru.com.cardiomagnyl.model.base.BaseModel;
import ru.com.cardiomagnyl.model.common.Error;
import ru.com.cardiomagnyl.model.common.Response;
import ru.com.cardiomagnyl.util.CallbackOne;
import ru.com.cardiomagnyl.util.ThreadHelper;

public abstract class BaseVolleyDataLoader extends BaseDataLoader {
    @SuppressWarnings("unchecked")
    protected <T_IN, T_OUT> void handleSuccessResponse(final Response response,
                                                       final BaseVolleyRequestHolder volleyRequestHolder,
                                                       final DataLoadSequence dataLoadSequence,
                                                       final CallbackOne<T_OUT> callbackOneOnSuccess,
                                                       final CallbackOne<Response> callbackOneOnError) {
        ThreadHelper.logThread("BaseVolleyDataLoader->handleSuccessResponse");

        if (volleyRequestHolder.getOnBeforeExtracted() != null) {
            volleyRequestHolder.getOnBeforeExtracted().execute(response);
        }

        T_IN resultIn = (T_IN) BaseModel.stringToObject(((Object) response.getData()).toString(), volleyRequestHolder.getTypeReference());

        if (resultIn == null) {
            handleErrorResponse(createInputDataError(), dataLoadSequence, callbackOneOnSuccess, callbackOneOnError);
        } else {
            if (volleyRequestHolder.getOnAfterExtracted() != null) {
                T_OUT resultOut = (T_OUT) volleyRequestHolder.getOnAfterExtracted().execute(resultIn);
                handleSuccessResponseHelper(resultOut, volleyRequestHolder, dataLoadSequence, callbackOneOnSuccess, callbackOneOnError);
            } else {
                handleSuccessResponseHelper((T_OUT) resultIn, volleyRequestHolder, dataLoadSequence, callbackOneOnSuccess, callbackOneOnError);
            }
        }
    }

    protected <T> void handleErrorResponse(final Response error,
                                           final DataLoadSequence dataLoadSequence,
                                           final CallbackOne<T> callbackOneOnSuccess,
                                           final CallbackOne<Response> callbackOneOnError) {
        ThreadHelper.logThread("BaseVolleyDataLoader->handleErrorResponse");
        handleResult(null, error, dataLoadSequence, callbackOneOnSuccess, callbackOneOnError);
    }

    protected Response dataToResponse(byte[] responseData) {
        Response response = null;
        try {
            String responseString = new String(responseData, "UTF-8");
            response = (Response) BaseModel.stringToObject(responseString, new TypeReference<Response>() {
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        return checkResponse(response);
    }

    protected Response stringToResponse(String responseString) {
        Response response = (Response) BaseModel.stringToObject(responseString, new TypeReference<Response>() {
        });
        return checkResponse(response);
    }

    protected Response checkResponse(Response response) {
        return response != null ?
                response :
                createInputDataError();
    }

    private Response createInputDataError() {
        return new Response
                .Builder(new Error(Status.INPUT_DATA_ERROR, Status.getDescription(Status.INPUT_DATA_ERROR)))
                .create();
    }

    private <T> void handleSuccessResponseHelper(final T result,
                                                 final BaseVolleyRequestHolder volleyRequestHolder,
                                                 final DataLoadSequence dataLoadSequence,
                                                 final CallbackOne<T> callbackOneOnSuccess,
                                                 final CallbackOne<Response> callbackOneOnError) {
        if (volleyRequestHolder.getOnStoreIntoDatabase() != null) {
            storeIntoDB(result, volleyRequestHolder);
        }
        handleResult(result, null, dataLoadSequence, callbackOneOnSuccess, callbackOneOnError);
    }

    private <T> void storeIntoDB(final T result, final BaseVolleyRequestHolder volleyRequestHolder) {
        volleyRequestHolder.getOnStoreIntoDatabase().execute(result);
    }
}