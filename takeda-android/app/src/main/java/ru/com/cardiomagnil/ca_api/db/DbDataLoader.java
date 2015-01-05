package ru.com.cardiomagnil.ca_api.db;

import com.j256.ormlite.stmt.QueryBuilder;

import java.util.ArrayList;
import java.util.List;

import ru.com.cardiomagnil.ca_api.DataLoadSequence;
import ru.com.cardiomagnil.ca_api.Status;
import ru.com.cardiomagnil.ca_api.base.BaseDataLoader;
import ru.com.cardiomagnil.ca_model.common.Ca_Error;
import ru.com.cardiomagnil.ca_model.common.Ca_Response;
import ru.com.cardiomagnil.util.CallbackOne;
import ru.com.cardiomagnil.util.ThtreadHelper;

public class DbDataLoader extends BaseDataLoader {
    @Override
    public <T> void invokeDataLoad(final DataLoadSequence dataLoadSequence,
                                   final CallbackOne<T> callbackOneOnSuccess,
                                   final CallbackOne<Ca_Response> callbackOneOnError) {
        final DbRequestHolder dbRequestHolder = (DbRequestHolder) dataLoadSequence.poll();

        List<Object> resultTmp = new ArrayList<Object>();
        Ca_Response error = null;

        List<QueryBuilder> queryBuilders = dbRequestHolder.getQueryBuilder();

        try {
            // FIXME: add to answers signs of request (necessary to properly matching responses to requests)
            for (QueryBuilder queryBuilder : queryBuilders) {
                Object currentResult = queryBuilder.query();
                resultTmp.add(currentResult);
            }
        } catch (Exception e) {
            e.printStackTrace();
            error = new Ca_Response
                    .Builder(new Ca_Error(Status.DB_ERROR, Status.getDescription(Status.DB_ERROR)))
                    .create();
            ThtreadHelper.logThread("DbDataLoader->invokeDataLoad->db_error");
        }

        Object result = queryBuilders != null && queryBuilders.size() == 1 && resultTmp != null && resultTmp.size() == 1
                ? resultTmp.get(0) : resultTmp;

        boolean errorIsNotPresent = error == null;
        boolean resultIsNotPresent = !isResultPresent(result);
        if (!errorIsNotPresent) {
            handleErrorResponse(error, dataLoadSequence, callbackOneOnSuccess, callbackOneOnError);
        } else if (errorIsNotPresent && resultIsNotPresent) {
            error = new Ca_Response
                    .Builder(new Ca_Error(Status.NO_DATA_ERROR, Status.getDescription(Status.NO_DATA_ERROR)))
                    .create();
            handleErrorResponse(error, dataLoadSequence, callbackOneOnSuccess, callbackOneOnError);
        } else {
            handleSuccessResponse(result, dbRequestHolder, dataLoadSequence, callbackOneOnSuccess, callbackOneOnError);
        }
    }

    private boolean isResultPresent(Object result) {
        if (result instanceof List) {
            for (Object resultElement : (List) result) {
                boolean resultElementIsPresent = !(resultElement == null || (resultElement instanceof List && ((List) resultElement).isEmpty()));
                if (resultElementIsPresent) return true;
            }
        } else {
            return result != null;
        }
        return false;
    }

    @SuppressWarnings("unchecked")
    protected <T_IN, T_OUT> void handleSuccessResponse(final T_IN resultIn,
                                                       final DbRequestHolder dbRequestHolder,
                                                       final DataLoadSequence dataLoadSequence,
                                                       final CallbackOne<T_OUT> callbackOneOnSuccess,
                                                       final CallbackOne<Ca_Response> callbackOneOnError) {
        ThtreadHelper.logThread("DbDataLoader->handleSuccessResponse");

        if (dbRequestHolder.getOnExtract() != null) {
            T_OUT resultOut = (T_OUT) dbRequestHolder.getOnExtract().execute(resultIn);
            handleResult(resultOut, null, dataLoadSequence, callbackOneOnSuccess, callbackOneOnError);
        } else {
            handleResult((T_OUT) resultIn, null, dataLoadSequence, callbackOneOnSuccess, callbackOneOnError);
        }
    }

    protected <T> void handleErrorResponse(final Ca_Response error,
                                           final DataLoadSequence dataLoadSequence,
                                           final CallbackOne<T> callbackOneOnSuccess,
                                           final CallbackOne<Ca_Response> callbackOneOnError) {
        ThtreadHelper.logThread("DbDataLoader->handleErrorResponse");
        handleResult(null, error, dataLoadSequence, callbackOneOnSuccess, callbackOneOnError);
    }
}
