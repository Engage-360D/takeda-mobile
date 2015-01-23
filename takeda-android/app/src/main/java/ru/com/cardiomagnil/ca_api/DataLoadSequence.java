package ru.com.cardiomagnil.ca_api;

import java.util.ArrayList;
import java.util.List;

import ru.com.cardiomagnil.ca_api.base.BaseRequestHolder;

public class DataLoadSequence {
    private final List<BaseRequestHolder> mSequence = new ArrayList<BaseRequestHolder>();

    public DataLoadSequence(List<BaseRequestHolder> sequence) {
        mSequence.addAll(sequence);
    }

    public BaseRequestHolder poll() {
        synchronized (mSequence) {
            BaseRequestHolder result = null;
            if (!mSequence.isEmpty()) {
                result = mSequence.get(0);
                mSequence.remove(0);
            }
            return result;
        }
    }

    public BaseRequestHolder peek() {
        synchronized (mSequence) {
            BaseRequestHolder result = null;
            if (!mSequence.isEmpty()) {
                result = mSequence.get(0);
            }
            return result;
        }
    }

    public boolean isEmpty() {
        synchronized (mSequence) {
            return mSequence.isEmpty();
        }
    }

    public static class Builder {
        private final List<BaseRequestHolder> mBuilderSequence = new ArrayList<BaseRequestHolder>();

        public Builder(BaseRequestHolder firstRequestHolder) {
            mBuilderSequence.add(firstRequestHolder);
        }

        public Builder addRequestHolder(BaseRequestHolder baseRequestHolder) {
            if (baseRequestHolder != null) {
                mBuilderSequence.add(baseRequestHolder);
            }
            return this;
        }

        public DataLoadSequence create() {
            return new DataLoadSequence(mBuilderSequence);
        }
    }
}
