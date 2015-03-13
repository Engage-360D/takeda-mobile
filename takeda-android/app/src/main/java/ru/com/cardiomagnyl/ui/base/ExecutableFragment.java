package ru.com.cardiomagnyl.ui.base;


import android.content.Context;

public interface ExecutableFragment {
    public void execute(Context context);

    public boolean isShowable();
}
