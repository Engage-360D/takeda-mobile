package ru.com.cardiomagnil.ca_api.http;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.Map;

public class CachedStringRequest extends StringRequest {
    public CachedStringRequest(int method, String url, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(method, url, listener, errorListener);
        setShouldCache(true);
    }

    @Override
    public String getCacheKey() {
        Map<String, String> params = null;

        try {
            params = getParams();
        } catch (AuthFailureError authFailureError) {
            authFailureError.printStackTrace();
        }

        String cacheKey = super.getCacheKey();
        if (getMethod() == Method.POST && params != null && !params.isEmpty()) {
            cacheKey += "?";
            for (Map.Entry<String, String> entry : params.entrySet()) {
                cacheKey += entry.getKey() + "=" + entry.getValue();
            }
        }

        return cacheKey;
    }

}
