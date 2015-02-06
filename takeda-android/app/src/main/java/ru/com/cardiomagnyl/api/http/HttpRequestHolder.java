package ru.com.cardiomagnyl.api.http;

import com.fasterxml.jackson.core.type.TypeReference;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import ru.com.cardiomagnyl.api.base.BaseDataLoader;
import ru.com.cardiomagnyl.api.base.BaseVolleyRequestHolder;
import ru.com.cardiomagnyl.util.CallbackOne;
import ru.com.cardiomagnyl.util.CallbackOneReturnable;

public class HttpRequestHolder extends BaseVolleyRequestHolder {
    private final Map<String, String> mHeaders;
    private byte[] mBody;
    private final HttpDataLoader mHttpDataLoader;

    private HttpRequestHolder(
            int method,
            String url,
            Map<String, String> params,
            TypeReference typeReference,
            Map<String, String> headers,
            byte[] body,
            CallbackOne onBeforeExtract,
            CallbackOneReturnable onOnAfterExtracted,
            CallbackOne onStoreIntoDatabase) {
        super(method, url, params, typeReference, onBeforeExtract, onOnAfterExtracted, onStoreIntoDatabase);
        mHeaders = headers;
        mBody = body;
        mHttpDataLoader = new HttpDataLoader();
    }

    public Map<String, String> getHeaders() {
        return mHeaders;
    }

    public byte[] getBody() {
        return mBody;
    }

    @Override
    public BaseDataLoader getDataLoader() {
        return mHttpDataLoader;
    }

    public static class Builder {
        private final int mBuilderMethod;
        private final String mBuilderUrl;
        private final TypeReference mBuildTypeReference;
        private Map<String, String> mBuilderParams = new HashMap<String, String>();
        private Map<String, String> mBuilderHeaders = new HashMap<String, String>();
        private byte[] mBuilderBody;
        private CallbackOne mBuilderOnBeforeExtract;
        private CallbackOneReturnable mBuilderOnAfterExtracted;
        private CallbackOne mBuilderOnStoreIntoDatabase;

        public Builder(int builderMethod, String builderUrl, TypeReference builderTypeReference) {
            mBuilderMethod = builderMethod;
            mBuilderUrl = builderUrl;
            mBuildTypeReference = builderTypeReference;
        }

        public Builder addParam(String key, String value) {
            mBuilderParams.put(key, value);
            return this;
        }

        public Builder addParams(Map<String, String> param) {
            mBuilderParams.putAll(param);
            return this;
        }

        public Builder addHeader(String key, String value) {
            mBuilderHeaders.put(key, value);
            return this;
        }

        public Builder addHeaders(Map<String, String> headers) {
            mBuilderHeaders.putAll(headers);
            return this;
        }

        public Builder setBody(byte[] body) {
            mBuilderBody = body;
            return this;
        }

        public Builder setBody(String body) {
            mBuilderBody = encodeString(body, DEFAULT_PARAMS_ENCODING);
            return this;
        }

        public Builder setOnBeforeExtract(CallbackOne onOnBeforeExtract) {
            mBuilderOnBeforeExtract = onOnBeforeExtract;
            return this;
        }

        public Builder setOnAfterExtracted(CallbackOneReturnable onAfterExtracted) {
            mBuilderOnAfterExtracted = onAfterExtracted;
            return this;
        }

        public Builder setOnStoreIntoDatabase(CallbackOne onStoreIntoDatabase) {
            mBuilderOnStoreIntoDatabase = onStoreIntoDatabase;
            return this;
        }

        public HttpRequestHolder create() {
            return new HttpRequestHolder(
                    mBuilderMethod,
                    mBuilderUrl,
                    mBuilderParams,
                    mBuildTypeReference,
                    mBuilderHeaders,
                    mBuilderBody,
                    mBuilderOnBeforeExtract,
                    mBuilderOnAfterExtracted,
                    mBuilderOnStoreIntoDatabase);
        }

        private byte[] encodeString(String stringToEncode, String paramsEncoding) {
            try {
                return stringToEncode.getBytes(paramsEncoding);
            } catch (UnsupportedEncodingException exception) {
                throw new RuntimeException("Encoding not supported: " + paramsEncoding, exception);
            }
        }

        private byte[] urlEncodeString(String stringToEncode, String paramsEncoding) {
            try {
                String stringEncoded = URLEncoder.encode(stringToEncode, paramsEncoding);
                return stringEncoded.getBytes(paramsEncoding);
            } catch (UnsupportedEncodingException exception) {
                throw new RuntimeException("Encoding not supported: " + paramsEncoding, exception);
            }
        }
    }
}