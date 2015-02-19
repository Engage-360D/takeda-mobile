package ru.com.cardiomagnyl.api.db;

import com.j256.ormlite.stmt.QueryBuilder;

import java.util.ArrayList;
import java.util.List;

import ru.com.cardiomagnyl.api.base.BaseDataLoader;
import ru.com.cardiomagnyl.api.base.BaseRequestHolder;
import ru.com.cardiomagnyl.util.CallbackOneReturnable;

public class DbRequestHolder extends BaseRequestHolder {
    /**
     * complement if necessary
     */
    public enum QueryMethod {
        query, queryForFirst
    }

    private final List<QueryBuilder> mQueryBuilder;
    private final QueryMethod mQueryMethod;
    private final CallbackOneReturnable mAfterExtracted;
    private final DbDataLoader mDbDataLoader;

    private DbRequestHolder(
            List<QueryBuilder> queryBuilder,
            QueryMethod queryMethod,
            CallbackOneReturnable afterExtracted) {
        mQueryBuilder = queryBuilder;
        mQueryMethod = queryMethod;
        mAfterExtracted = afterExtracted;
        mDbDataLoader = new DbDataLoader();
    }

    public List<QueryBuilder> getQueryBuilder() {
        return mQueryBuilder;
    }

    public QueryMethod getQueryMethod() {
        return mQueryMethod;
    }

    public CallbackOneReturnable getAfterExtracted() {
        return mAfterExtracted;
    }

    @Override
    public BaseDataLoader getDataLoader() {
        return mDbDataLoader;
    }

    public static class Builder {
        private final List<QueryBuilder> mBuilderQueryBuilder = new ArrayList<QueryBuilder>();
        private QueryMethod mBuilderQueryMethod = QueryMethod.query;
        private CallbackOneReturnable mBuilderAfterExtracted;

        public Builder(QueryBuilder queryBuilder) {
            mBuilderQueryBuilder.add(queryBuilder);
        }

        public Builder addQueryBuilder(QueryBuilder queryBuilder) {
            mBuilderQueryBuilder.add(queryBuilder);
            return this;
        }

        public Builder setQueryMethod(QueryMethod queryMethod) {
            mBuilderQueryMethod = queryMethod;
            return this;
        }

        public Builder setAfterExtracted(CallbackOneReturnable afterExtracted) {
            mBuilderAfterExtracted = afterExtracted;
            return this;
        }

        public DbRequestHolder create() {
            return new DbRequestHolder(
                    mBuilderQueryBuilder,
                    mBuilderQueryMethod,
                    mBuilderAfterExtracted);
        }
    }
}
