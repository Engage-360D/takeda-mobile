package ru.com.cardiomagnil.ca_api.http;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import java.net.URLEncoder;
import java.util.Map;

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
        final String urlWithParams = buildUrlWithParams(httpRequestHolder.getMethod(), httpRequestHolder.getUrl(), httpRequestHolder.getParams());

        CachedStringRequest cachedStringRequest = new CachedStringRequest(
                httpRequestHolder.getMethod(),
                urlWithParams,
                new Response.Listener<String>() {
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
                },
                new Response.ErrorListener() {
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
                }
        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = httpRequestHolder.getHeaders();
                // FIXME!
//                headers.put("Authorization", Url.BASIC_AUTHORIZATION);
                return headers;
            }

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = httpRequestHolder.getParams();
                return params;
            }
        };

        // Adding request to request queue
        HttpHelper.getInstance().addToRequestQueue(cachedStringRequest);
    }

    private String buildUrlWithParams(int method, String url, Map<String, String> params) {
        String paramsStrig = "";
        if (method == Request.Method.GET && params != null && !params.isEmpty()) {
            for (Map.Entry<String, String> entry : params.entrySet()) {
                paramsStrig += URLEncoder.encode(entry.getKey()) + "=" + URLEncoder.encode(entry.getValue()) + "&";
            }
        }
        return paramsStrig.isEmpty() ? url : url + "?" + paramsStrig;
    }
}