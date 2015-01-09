package ru.com.cardiomagnil.ca_api.base;


import com.fasterxml.jackson.core.type.TypeReference;

import ru.com.cardiomagnil.ca_api.DataLoadSequence;
import ru.com.cardiomagnil.ca_api.Status;
import ru.com.cardiomagnil.ca_model.base.BaseModel;
import ru.com.cardiomagnil.ca_model.common.Ca_Error;
import ru.com.cardiomagnil.ca_model.common.Ca_Response;
import ru.com.cardiomagnil.util.CallbackOne;
import ru.com.cardiomagnil.util.ThtreadHelper;

public abstract class BaseVolleyDataLoader extends BaseDataLoader {
    @SuppressWarnings("unchecked")
    protected <T_IN, T_OUT> void handleSuccessResponse(final Ca_Response response,
                                                       final BaseVolleyRequestHolder volleyRequestHolder,
                                                       final DataLoadSequence dataLoadSequence,
                                                       final CallbackOne<T_OUT> callbackOneOnSuccess,
                                                       final CallbackOne<Ca_Response> callbackOneOnError) {
        ThtreadHelper.logThread("BaseVolleyDataLoader->handleSuccessResponse");

        if (volleyRequestHolder.getOnBeforeExtracted() != null) {
            volleyRequestHolder.getOnBeforeExtracted().execute( response.getData());
        }

        T_IN resultIn = (T_IN) BaseModel.stringToObject(((Object) response.getData()).toString(), volleyRequestHolder.getTypeReference());

        if (volleyRequestHolder.getOnAfterExtracted() != null) {
            T_OUT resultOut = (T_OUT) volleyRequestHolder.getOnAfterExtracted().execute(resultIn);
            handleSuccessResponseHelper(resultOut, volleyRequestHolder, dataLoadSequence, callbackOneOnSuccess, callbackOneOnError);
        } else {
            handleSuccessResponseHelper((T_OUT) resultIn, volleyRequestHolder, dataLoadSequence, callbackOneOnSuccess, callbackOneOnError);
        }
    }

    protected <T> void handleErrorResponse(final Ca_Response error,
                                           final DataLoadSequence dataLoadSequence,
                                           final CallbackOne<T> callbackOneOnSuccess,
                                           final CallbackOne<Ca_Response> callbackOneOnError) {
        ThtreadHelper.logThread("BaseVolleyDataLoader->handleErrorResponse");
        handleResult(null, error, dataLoadSequence, callbackOneOnSuccess, callbackOneOnError);
    }

    protected Ca_Response dataToResponse(byte[] responseData) {
        Ca_Response response = null;
        try {
            String responseString = new String(responseData, "UTF-8");
            response = (Ca_Response) BaseModel.stringToObject(responseString, new TypeReference<Ca_Response>() {
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        return checkResponse(response);
    }

    protected Ca_Response stringToResponse(String responseString) {
        Ca_Response response = (Ca_Response) BaseModel.stringToObject(responseString, new TypeReference<Ca_Response>() {
        });
        return checkResponse(response);
    }

    protected Ca_Response checkResponse(Ca_Response response) {
        return response != null ?
                response :
                new Ca_Response
                        .Builder(new Ca_Error(Status.INPUT_DATA_ERROR, Status.getDescription(Status.INPUT_DATA_ERROR)))
                        .create();
    }

    private <T> void handleSuccessResponseHelper(final T result,
                                                 final BaseVolleyRequestHolder volleyRequestHolder,
                                                 final DataLoadSequence dataLoadSequence,
                                                 final CallbackOne<T> callbackOneOnSuccess,
                                                 final CallbackOne<Ca_Response> callbackOneOnError) {
        if (volleyRequestHolder.getOnStoreIntoDatabase() != null) {
            storeIntoDB(result, volleyRequestHolder);
        }
        handleResult(result, null, dataLoadSequence, callbackOneOnSuccess, callbackOneOnError);
    }

    private <T> void storeIntoDB(final T result, final BaseVolleyRequestHolder volleyRequestHolder) {
        volleyRequestHolder.getOnStoreIntoDatabase().execute(result);
    }
}