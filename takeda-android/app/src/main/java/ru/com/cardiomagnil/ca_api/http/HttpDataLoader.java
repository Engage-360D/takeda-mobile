package ru.com.cardiomagnil.ca_api.http;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import ru.com.cardiomagnil.ca_api.CachedStringRequest;
import ru.com.cardiomagnil.ca_api.DataLoadSequence;
import ru.com.cardiomagnil.ca_api.Status;
import ru.com.cardiomagnil.ca_api.base.BaseVolleyDataLoader;
import ru.com.cardiomagnil.ca_model.common.Ca_Error;
import ru.com.cardiomagnil.ca_model.common.Ca_Response;
import ru.com.cardiomagnil.util.Callback;
import ru.com.cardiomagnil.util.CallbackOne;
import ru.com.cardiomagnil.util.ThtreadHelper;

public class HttpDataLoader extends BaseVolleyDataLoader {
    @Override
    public <T> void invokeDataLoad(final DataLoadSequence dataLoadSequence,
                                   final CallbackOne<T> callbackOneOnSuccess,
                                   final CallbackOne<Ca_Response> callbackOneOnError) {
        final HttpRequestHolder httpRequestHolder = (HttpRequestHolder) dataLoadSequence.poll();
        final Response.Listener<String> successListener = new Response.Listener<String>() {
            @Override
            public void onResponse(final String responseString) {
                ThtreadHelper.startNotInMain(new Callback() {
                    @Override
                    public void execute() {
                        Ca_Response response = stringToResponse(responseString);
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
        final Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(final VolleyError volleyError) {
                ThtreadHelper.startNotInMain(new Callback() {
                    @Override
                    public void execute() {
                        if (volleyError.networkResponse == null || volleyError.networkResponse.data == null) {
                            ThtreadHelper.logThread("HttpDataLoader->onErrorResponse->error_null");
                            Ca_Response error = new Ca_Response
                                    .Builder(new Ca_Error(Status.LAN_ERROR, Status.getDescription(Status.LAN_ERROR)))
                                    .create();
                            handleErrorResponse(error, dataLoadSequence, callbackOneOnSuccess, callbackOneOnError);
                        } else {
                            ThtreadHelper.logThread("HttpDataLoader->onErrorResponse->error");
                            handleErrorResponse(dataToResponse(volleyError.networkResponse.data), dataLoadSequence, callbackOneOnSuccess, callbackOneOnError);
                        }
                    }
                });
            }
        };

        final String fakeResponse = "{\"links\":{\"users.region\":{\"href\":\"/api/v1/regions/{users.region}\",\"type\":\"regions\"}},\"data\":{\"id\":\"8\",\"email\":\"y.andreyko3@gmail.com\",\"firstname\":\"XXXX\",\"lastname\":\"XXXX\",\"birthday\":\"1990-10-10T00:00:00+00:00\",\"vkontakteId\":null,\"facebookId\":null,\"specializationExperienceYears\":null,\"specializationGraduationDate\":null,\"specializationInstitutionAddress\":null,\"specializationInstitutionName\":null,\"specializationInstitutionPhone\":null,\"specializationName\":null,\"roles\":[\"ROLE_USER\", \"ROLE_Test1\", \"ROLE_Test2\"],\"isEnabled\":false,\"links\":{\"region\":\"1\"}}}";
        final Response.Listener<String> fakeSuccessListener = new Response.Listener<String>() {
            @Override
            public void onResponse(final String responseString) {
                Ca_Response response = stringToResponse(fakeResponse);
                if (response.isSuccessful()) {
                    handleSuccessResponse(response, httpRequestHolder, dataLoadSequence, callbackOneOnSuccess, callbackOneOnError);
                } else {
                    handleErrorResponse(response, dataLoadSequence, callbackOneOnSuccess, callbackOneOnError);
                }
            }
        };
        final Response.ErrorListener fakeErrorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(final VolleyError volleyError) {
                Ca_Response response = stringToResponse(fakeResponse);
                if (response.isSuccessful()) {
                    handleSuccessResponse(response, httpRequestHolder, dataLoadSequence, callbackOneOnSuccess, callbackOneOnError);
                } else {
                    handleErrorResponse(response, dataLoadSequence, callbackOneOnSuccess, callbackOneOnError);
                }
            }
        };

//        final String urlWithParams = buildUrlWithParams(httpRequestHolder.getMethod(), httpRequestHolder.getUrl(), httpRequestHolder.getParams());
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