package ru.com.cardiomagnil.ca_api.db;

import com.j256.ormlite.stmt.QueryBuilder;

import java.util.ArrayList;
import java.util.List;

import ru.com.cardiomagnil.ca_api.base.BaseDataLoader;
import ru.com.cardiomagnil.ca_api.base.BaseRequestHolder;
import ru.com.cardiomagnil.util.CallbackOneReturnable;

public class DbRequestHolder extends BaseRequestHolder {
    private final List<QueryBuilder> mQueryBuilder;
    protected CallbackOneReturnable mOnExtract;
    private final DbDataLoader mDbDataLoader;

    private DbRequestHolder(
            List<QueryBuilder> queryBuilder,
            CallbackOneReturnable onExtract) {
        mQueryBuilder = queryBuilder;
        mOnExtract = onExtract;
        mDbDataLoader = new DbDataLoader();
    }

    public List<QueryBuilder> getQueryBuilder() {
        return mQueryBuilder;
    }

    public CallbackOneReturnable getOnExtract() {
        return mOnExtract;
    }

    @Override
    public BaseDataLoader getDataLoader() {
        return mDbDataLoader;
    }

    public static class Builder {
        private final List<QueryBuilder> mBuilderQueryBuilder = new ArrayList<QueryBuilder>();
        private CallbackOneReturnable mBuilderOnExtract;

        public Builder(QueryBuilder queryBuilder) {
            mBuilderQueryBuilder.add(queryBuilder);
        }

        public Builder addQueryBuilder(QueryBuilder queryBuilder) {
            mBuilderQueryBuilder.add(queryBuilder);
            return this;
        }

        public Builder setOnExtract(CallbackOneReturnable onOnExtract) {
            mBuilderOnExtract = onOnExtract;
            return this;
        }

        public DbRequestHolder create() {
            return new DbRequestHolder(
                    mBuilderQueryBuilder,
                    mBuilderOnExtract);
        }
    }
}
