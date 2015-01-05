package ru.com.cardiomagnil.ca_api.http;

import com.fasterxml.jackson.core.type.TypeReference;

import java.util.HashMap;
import java.util.Map;

import ru.com.cardiomagnil.ca_api.base.BaseDataLoader;
import ru.com.cardiomagnil.ca_api.base.BaseVolleyRequestHolder;
import ru.com.cardiomagnil.util.CallbackOne;
import ru.com.cardiomagnil.util.CallbackOneReturnable;

public class HttpRequestHolder extends BaseVolleyRequestHolder {
    private final Map<String, String> mHeaders;
    private final HttpDataLoader mHttpDataLoader;

    private HttpRequestHolder(
            int method,
            String url,
            Map<String, String> params,
            TypeReference typeReference,
            Map<String, String> headers,
            CallbackOneReturnable onOnExtract,
            CallbackOne onStoreIntoDatabase) {
        super(method, url, params, typeReference, onOnExtract, onStoreIntoDatabase);
        mHeaders = headers;
        mHttpDataLoader = new HttpDataLoader();
    }

    public Map<String, String> getHeaders() {
        return mHeaders;
    }

    @Override
    public BaseDataLoader getDataLoader() {
        return mHttpDataLoader;
    }

    public static class Builder {
        private final int mBuilderMethod;
        private final String mBuilderUrl;
        private final TypeReference mBuildeTypeReference;
        private Map<String, String> mBuilderParams = new HashMap<String, String>();
        private Map<String, String> mBuilderHeaders = new HashMap<String, String>();
        private CallbackOneReturnable mBuilderOnExtract;
        private CallbackOne mBuilderOnStoreIntoDatabase;

        public Builder(int builderMethod, String builderUrl, TypeReference builderTypeReference) {
            mBuilderMethod = builderMethod;
            mBuilderUrl = builderUrl;
            mBuildeTypeReference = builderTypeReference;
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

        public Builder setOnExtract(CallbackOneReturnable onOnExtract) {
            mBuilderOnExtract = onOnExtract;
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
                    mBuildeTypeReference,
                    mBuilderHeaders,
                    mBuilderOnExtract,
                    mBuilderOnStoreIntoDatabase);
        }
    }
}