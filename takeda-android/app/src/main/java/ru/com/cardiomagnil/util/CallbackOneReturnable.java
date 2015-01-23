package ru.com.cardiomagnil.util;

public interface CallbackOneReturnable<T_IN, T_OUT> {
    T_OUT execute(T_IN argument);
}
