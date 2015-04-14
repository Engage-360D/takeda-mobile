package ru.com.cardiomagnyl.util.schedule;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import java.util.List;

import ru.com.cardiomagnyl.ui.slidingmenu.menu.SlidingMenuActivity;
import ru.com.cardiomagnyl.ui.start.SplashActivity;
import ru.com.cardiomagnyl.ui.start.StartActivity;

public class NotificationBroadcastReceiver extends BroadcastReceiver {
    private enum Activities {splash_activity, start_ativity, sliding_menu_activity}

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent onNotificationIntent = new Intent(context, SplashActivity.class);
        switch (mustRunSlidinMenu(context)) {
            case splash_activity:
                onNotificationIntent = new Intent(context, SplashActivity.class);
                break;
            case start_ativity:
                onNotificationIntent = new Intent(context, StartActivity.class);
                break;
            case sliding_menu_activity:
                onNotificationIntent = new Intent(context, SlidingMenuActivity.class);
                break;
        }
        onNotificationIntent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(onNotificationIntent);
    }

    private Activities mustRunSlidinMenu(Context context) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> tasks = activityManager.getRunningTasks(Integer.MAX_VALUE);

        String splashActivity = SplashActivity.class.getName();
        String startActivity = StartActivity.class.getName();
        String slidingMenuActivity = SlidingMenuActivity.class.getName();
        for (ActivityManager.RunningTaskInfo task : tasks) {
            String className = task.baseActivity.getClassName();

            if (splashActivity.equalsIgnoreCase(className)) return Activities.splash_activity;
            else if (startActivity.equalsIgnoreCase(className)) return Activities.start_ativity;
            else if (slidingMenuActivity.equalsIgnoreCase(className))
                return Activities.sliding_menu_activity;

        }

        return Activities.splash_activity;
    }

}
