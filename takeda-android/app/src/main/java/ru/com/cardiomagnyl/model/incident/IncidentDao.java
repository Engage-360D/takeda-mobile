package ru.com.cardiomagnyl.model.incident;

import android.os.Handler;

import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.support.ConnectionSource;

import java.sql.SQLException;

import ru.com.cardiomagnyl.model.common.Dummy;
import ru.com.cardiomagnyl.model.common.Response;
import ru.com.cardiomagnyl.model.test.TestPage;
import ru.com.cardiomagnyl.model.token.Token;
import ru.com.cardiomagnyl.util.CallbackOne;

public class IncidentDao extends BaseDaoImpl<TestPage, Integer> {
    public IncidentDao(ConnectionSource connectionSource, Class<TestPage> dataClass) throws SQLException {
        super(connectionSource, dataClass);
    }

    public static void report(final Incident incident,
                            final Token token,
                            final CallbackOne<Dummy> onSuccess,
                            final CallbackOne<Response> onFailure) {
        // FIXME: implement body when server will be ready
        runDummy(incident, token, onSuccess, onFailure);
    }

    private static void runDummy(final Incident incident,
                                 final Token token,
                                 final CallbackOne<Dummy> onSuccess,
                                 final CallbackOne<Response> onFailure) {
        final Handler uiHandler = new Handler();
        new Thread() {
            @Override
            public void run() {
                try {
                    sleep(3000);
                    uiHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            onSuccess.execute(new Dummy());
                        }
                    });
                    this.interrupt();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

}
