package ru.com.cardiomagnil.ca_api.cache;

import com.fasterxml.jackson.core.type.TypeReference;

import java.util.HashMap;
import java.util.Map;

import ru.com.cardiomagnil.ca_api.base.BaseDataLoader;
import ru.com.cardiomagnil.ca_api.base.BaseVolleyRequestHolder;
import ru.com.cardiomagnil.util.CallbackOne;
import ru.com.cardiomagnil.util.CallbackOneReturnable;

public class CacheRequestHolder extends BaseVolleyRequestHolder {
    private final CacheDataLoader mCacheDataLoader;

    private CacheRequestHolder(
            int method,
            String url,
            TypeReference typeReference,
            Map<String, String> params,
            CallbackOneReturnable onOnExtract,
            CallbackOne onStoreIntoDatabase) {
        super(method, url, params, typeReference, onOnExtract, onStoreIntoDatabase);
        mCacheDataLoader = new CacheDataLoader();
    }

    @Override
    public BaseDataLoader getDataLoader() {
        return mCacheDataLoader;
    }

    public static class Builder {
        private final int mBuilderMethod;
        private final String mBuilderUrl;
        private final TypeReference mBuildeTypeReference;
        private Map<String, String> mBuilderParams = new HashMap<String, String>();
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

        public Builder setOnExtract(CallbackOneReturnable onOnExtract) {
            mBuilderOnExtract = onOnExtract;
            return this;
        }

        public Builder setOnStoreIntoDatabase(CallbackOne onStoreIntoDatabase) {
            mBuilderOnStoreIntoDatabase = onStoreIntoDatabase;
            return this;
        }

        public CacheRequestHolder create() {
            return new CacheRequestHolder(
                    mBuilderMethod,
                    mBuilderUrl,
                    mBuildeTypeReference,
                    mBuilderParams,
                    mBuilderOnExtract,
                    mBuilderOnStoreIntoDatabase);
        }
    }
}
