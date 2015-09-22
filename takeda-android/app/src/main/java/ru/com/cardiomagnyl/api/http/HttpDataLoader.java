package ru.com.cardiomagnyl.api.http;

import com.android.volley.VolleyError;

import ru.com.cardiomagnyl.api.CachedStringRequest;
import ru.com.cardiomagnyl.api.DataLoadSequence;
import ru.com.cardiomagnyl.api.Status;
import ru.com.cardiomagnyl.api.base.BaseVolleyDataLoader;
import ru.com.cardiomagnyl.model.common.Error;
import ru.com.cardiomagnyl.model.common.Response;
import ru.com.cardiomagnyl.util.Callback;
import ru.com.cardiomagnyl.util.CallbackOne;
import ru.com.cardiomagnyl.util.ThreadHelper;

public class HttpDataLoader extends BaseVolleyDataLoader {
    @Override
    public <T> void invokeDataLoad(final DataLoadSequence dataLoadSequence,
                                   final CallbackOne<T> callbackOneOnSuccess,
                                   final CallbackOne<Response> callbackOneOnError) {
        final HttpRequestHolder httpRequestHolder = (HttpRequestHolder) dataLoadSequence.poll();
        final com.android.volley.Response.Listener<String> successListener = new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(final String responseString) {
                ThreadHelper.startNotInMain(new Callback() {
                    @Override
                    public void execute() {
                        Response response = stringToResponse(responseString);

                        if (response.isSuccessful()) {
                            ThreadHelper.logThread("HttpDataLoader->onResponse->success");
                            handleSuccessResponse(response, httpRequestHolder, dataLoadSequence, callbackOneOnSuccess, callbackOneOnError);
                        } else {
                            ThreadHelper.logThread("HttpDataLoader->onResponse->error");
                            handleErrorResponse(response, dataLoadSequence, callbackOneOnSuccess, callbackOneOnError);
                        }
                    }
                });
            }
        };
        final com.android.volley.Response.ErrorListener errorListener = new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(final VolleyError volleyError) {
                ThreadHelper.startNotInMain(new Callback() {
                    @Override
                    public void execute() {
                        if (volleyError.networkResponse == null || volleyError.networkResponse.data == null) {
                            ThreadHelper.logThread("HttpDataLoader->onErrorResponse->error_null");
                            Response error = new Response
                                    .Builder(new Error(Status.LAN_ERROR, Status.getDescription(Status.LAN_ERROR)))
                                    .create();
                            handleErrorResponse(error, dataLoadSequence, callbackOneOnSuccess, callbackOneOnError);
                        } else {
                            ThreadHelper.logThread("HttpDataLoader->onErrorResponse->error");
                            int errorCode = volleyError.networkResponse.statusCode;
                            String errorDescription = Status.getDescription(errorCode);
                            Response responseError = new Response.Builder(new Error(errorCode, errorDescription)).create();
                            handleErrorResponse(responseError, dataLoadSequence, callbackOneOnSuccess, callbackOneOnError);
                        }
                    }
                });
            }
        };

        CachedStringRequest cachedStringRequest = new CachedStringRequest(
                httpRequestHolder.getMethod(),
                httpRequestHolder.getUrl(),
                httpRequestHolder.getHeaders(),
                httpRequestHolder.getParams(),
                httpRequestHolder.getBody(),
                successListener,
                errorListener
        );

        // Adding request to request queue
        HttpHelper.getInstance().addToRequestQueue(cachedStringRequest);
    }
}