package ru.com.cardiomagnyl.util.schedule;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;

import java.util.List;

import ru.com.cardiomagnyl.application.AppSharedPreferences;
import ru.com.cardiomagnyl.model.common.Response;
import ru.com.cardiomagnyl.model.pill.Pill;
import ru.com.cardiomagnyl.model.pill.PillDao;
import ru.com.cardiomagnyl.model.token.Token;
import ru.com.cardiomagnyl.model.token.TokenDao;
import ru.com.cardiomagnyl.util.CallbackOne;

public class BootBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        getToken();
    }

    private void getToken() {
        String tokenId = (String) AppSharedPreferences.get(AppSharedPreferences.Preference.tokenId);
        if (TextUtils.isEmpty(tokenId)) return;
        TokenDao.getByTokenId(
                tokenId,
                new CallbackOne<Token>() {
                    @Override
                    public void execute(Token token) {
                        getPillDatabase(token);
                    }
                },
                new CallbackOne<Response>() {
                    @Override
                    public void execute(Response responseError) {/* does nothing */}
                }
        );
    }

    private void getPillDatabase(Token token) {
        PillDao.getAll(
                token,
                new CallbackOne<List<Pill>>() {
                    @Override
                    public void execute(List<Pill> pillsList) {
                        PillsScheduler.setAll(pillsList);
                    }
                },
                new CallbackOne<Response>() {
                    @Override
                    public void execute(Response responseError) {/* does nothing */}
                },
                PillDao.Source.database
        );
    }

}