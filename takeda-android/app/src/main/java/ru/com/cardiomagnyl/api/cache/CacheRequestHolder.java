package ru.com.cardiomagnyl.api.cache;

import com.fasterxml.jackson.core.type.TypeReference;

import java.util.HashMap;
import java.util.Map;

import ru.com.cardiomagnyl.api.base.BaseDataLoader;
import ru.com.cardiomagnyl.api.base.BaseVolleyRequestHolder;
import ru.com.cardiomagnyl.util.CallbackOne;
import ru.com.cardiomagnyl.util.CallbackOneReturnable;

public class CacheRequestHolder extends BaseVolleyRequestHolder {
    private final CacheDataLoader mCacheDataLoader;

    private CacheRequestHolder(
            int method,
            String url,
            TypeReference typeReference,
            Map<String, String> params,
            CallbackOne onBeforeExtract,
            CallbackOneReturnable onAfterExtracted,
            CallbackOne onStoreIntoDatabase) {
        super(method, url, params, typeReference, onBeforeExtract, onAfterExtracted, onStoreIntoDatabase);
        mCacheDataLoader = new CacheDataLoader();
    }

    @Override
    public BaseDataLoader getDataLoader() {
        return mCacheDataLoader;
    }

    public static class Builder {
        private final int mBuilderMethod;
        private final String mBuilderUrl;
        private final TypeReference mBuildTypeReference;
        private Map<String, String> mBuilderParams = new HashMap<String, String>();
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

        public Builder setOnBeforeExtract(CallbackOne onOnBeforeExtract) {
            mBuilderOnBeforeExtract = onOnBeforeExtract;
            return this;
        }

        public Builder setOnAfterExtracted(CallbackOneReturnable onOnAfterExtracted) {
            mBuilderOnAfterExtracted = onOnAfterExtracted;
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
                    mBuildTypeReference,
                    mBuilderParams,
                    mBuilderOnBeforeExtract,
                    mBuilderOnAfterExtracted,
                    mBuilderOnStoreIntoDatabase);
        }
    }
}
