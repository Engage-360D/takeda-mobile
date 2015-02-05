package ru.com.cardiomagnil.ca_api.http;

import com.android.volley.VolleyError;

import ru.com.cardiomagnil.ca_api.CachedStringRequest;
import ru.com.cardiomagnil.ca_api.DataLoadSequence;
import ru.com.cardiomagnil.ca_api.Status;
import ru.com.cardiomagnil.ca_api.base.BaseVolleyDataLoader;
import ru.com.cardiomagnil.model.common.Error;
import ru.com.cardiomagnil.model.common.Response;
import ru.com.cardiomagnil.util.Callback;
import ru.com.cardiomagnil.util.CallbackOne;
import ru.com.cardiomagnil.util.ThtreadHelper;

public class HttpDataLoader extends BaseVolleyDataLoader {
    @Override
    public <T> void invokeDataLoad(final DataLoadSequence dataLoadSequence,
                                   final CallbackOne<T> callbackOneOnSuccess,
                                   final CallbackOne<Response> callbackOneOnError) {
        final HttpRequestHolder httpRequestHolder = (HttpRequestHolder) dataLoadSequence.poll();
        final com.android.volley.Response.Listener<String> successListener = new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(final String responseString) {
                ThtreadHelper.startNotInMain(new Callback() {
                    @Override
                    public void execute() {
                        Response response = stringToResponse(responseString);
                        if (response.isSuccessful()) {
                            ThtreadHelper.logThread("HttpDataLoader->onResponse->success");
                            handleSuccessResponse(response, httpRequestHolder, dataLoadSequence, callbackOneOnSuccess, callbackOneOnError);
                        } else {
                            ThtreadHelper.logThread("HttpDataLoader->onResponse->error");
                            handleErrorResponse(response, dataLoadSequence, callbackOneOnSuccess, callbackOneOnError);
                        }
                    }
                });
            }
        };
        final com.android.volley.Response.ErrorListener errorListener = new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(final VolleyError volleyError) {
                ThtreadHelper.startNotInMain(new Callback() {
                    @Override
                    public void execute() {
                        if (volleyError.networkResponse == null || volleyError.networkResponse.data == null) {
                            ThtreadHelper.logThread("HttpDataLoader->onErrorResponse->error_null");
                            Response error = new Response
                                    .Builder(new Error(Status.LAN_ERROR, Status.getDescription(Status.LAN_ERROR)))
                                    .create();
                            handleErrorResponse(error, dataLoadSequence, callbackOneOnSuccess, callbackOneOnError);
                        } else {
                            ThtreadHelper.logThread("HttpDataLoader->onErrorResponse->error");
                            int errorCode = volleyError.networkResponse.statusCode;
                            String errorDescription = Status.getDescription(errorCode);
                            Response responseError = new Response.Builder(new Error(errorCode, errorDescription)).create();
                            handleErrorResponse(responseError, dataLoadSequence, callbackOneOnSuccess, callbackOneOnError);
                        }
                    }
                });
            }
        };

        final String fakeResponse = "{\"links\":{\"users.region\":{\"href\":\"/api/v1/regions/{users.region}\",\"type\":\"regions\"}},\"data\":{\"id\":\"8\",\"email\":\"y.andreyko3@gmail.com\",\"firstname\":\"XXXX\",\"lastname\":\"XXXX\",\"birthday\":\"1990-10-10T00:00:00+00:00\",\"vkontakteId\":null,\"facebookId\":null,\"specializationExperienceYears\":null,\"specializationGraduationDate\":null,\"specializationInstitutionAddress\":null,\"specializationInstitutionName\":null,\"specializationInstitutionPhone\":null,\"specializationName\":null,\"roles\":[\"ROLE_USER\", \"ROLE_Test1\", \"ROLE_Test2\"],\"isEnabled\":false,\"links\":{\"region\":\"1\"}}}";
        final com.android.volley.Response.Listener<String> fakeSuccessListener = new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(final String responseString) {
                Response response = stringToResponse(fakeResponse);
                if (response.isSuccessful()) {
                    handleSuccessResponse(response, httpRequestHolder, dataLoadSequence, callbackOneOnSuccess, callbackOneOnError);
                } else {
                    handleErrorResponse(response, dataLoadSequence, callbackOneOnSuccess, callbackOneOnError);
                }
            }
        };
        final com.android.volley.Response.ErrorListener fakeErrorListener = new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(final VolleyError volleyError) {
                Response response = stringToResponse(fakeResponse);
                if (response.isSuccessful()) {
                    handleSuccessResponse(response, httpRequestHolder, dataLoadSequence, callbackOneOnSuccess, callbackOneOnError);
                } else {
                    handleErrorResponse(response, dataLoadSequence, callbackOneOnSuccess, callbackOneOnError);
                }
            }
        };

        CachedStringRequest cachedStringRequest = new CachedStringRequest(
                httpRequestHolder.getMethod(),
                httpRequestHolder.getUrl(),
                httpRequestHolder.getHeaders(),
                httpRequestHolder.getParams(),
                httpRequestHolder.getBody(),
//                fakeSuccessListener,
//                fakeErrorListener
                successListener,
                errorListener
        );

        // Adding request to request queue
        HttpHelper.getInstance().addToRequestQueue(cachedStringRequest);
    }
}