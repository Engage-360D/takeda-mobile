package ru.com.cardiomagnyl.social;

public interface Callback<T> {
	void execute(T arg);
}
