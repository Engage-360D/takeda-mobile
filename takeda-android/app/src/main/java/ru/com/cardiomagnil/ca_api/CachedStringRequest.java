package ru.com.cardiomagnil.ca_api;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.net.URLEncoder;
import java.util.Map;

public class CachedStringRequest extends StringRequest {
    private final int mMethod;
    private final Map<String, String> mHeaders;
    private final Map<String, String> mParams;
    private final String mContentType;
    private final byte[] mBody;

    public CachedStringRequest(int method,
                               String url,
                               Map<String, String> headers,
                               Map<String, String> params,
                               byte[] body,
                               Response.Listener<String> listener,
                               Response.ErrorListener errorListener) {
        super(method, buildUrlWithParams(method, url, params), listener, errorListener);

        mMethod = method;
        mContentType = findAndRemoveContentType(headers);
        mHeaders = headers;
        mParams = params;
        mBody = body;

        setShouldCache(true);
    }

    private String findAndRemoveContentType(Map<String, String> headers) {
        Map.Entry<String, String> contentTypeEntry = null;
        if (headers != null && !headers.isEmpty()) {
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                String key = entry.getKey();
                if (key != null && "content-type".equals(key.toLowerCase())) {
                    contentTypeEntry = entry;
                    break;
                }
            }
        }

        if (contentTypeEntry != null) {
            headers.remove(contentTypeEntry.getKey());
            return contentTypeEntry.getValue();
        }

        return null;
    }

    @Override
    public String getCacheKey() {
        super.getCacheKey();

        Map<String, String> params = getParams();

        String cacheKey = super.getCacheKey();
        if ((getMethod() == Method.POST || getMethod() == Method.PUT) && params != null && !params.isEmpty()) {
            cacheKey += "?";
            for (Map.Entry<String, String> entry : params.entrySet()) {
                cacheKey += entry.getKey() + "=" + entry.getValue();
            }
        }

        return cacheKey;
    }

    @Override
    public Map<String, String> getHeaders() {
        // FIXME!
        // mHeaders.put("Authorization", Url.BASIC_AUTHORIZATION);
        return mHeaders;
    }

    @Override
    protected Map<String, String> getParams() {
        return mParams;
    }

    @Override
    public byte[] getBody() throws AuthFailureError {
        if (mMethod == Method.GET) {
            return mBody;
        } else if (mMethod == Method.POST || mMethod == Method.PUT ) {
            return mBody != null ? mBody : super.getBody();
        } else {
            return super.getBody();
        }
    }

    @Override
    public String getBodyContentType() {
        return mContentType != null ? mContentType : super.getBodyContentType();
    }

    private static String buildUrlWithParams(int method, String url, Map<String, String> params) {
        String paramsStrig = "";
        if (method == Request.Method.GET && params != null && !params.isEmpty()) {
            for (Map.Entry<String, String> entry : params.entrySet()) {
                paramsStrig += URLEncoder.encode(entry.getKey()) + "=" + URLEncoder.encode(entry.getValue()) + "&";
            }
        }
        return paramsStrig.isEmpty() ? url : url + "?" + paramsStrig;
    }
}
