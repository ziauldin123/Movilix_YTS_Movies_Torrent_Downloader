

package com.movilix.torrant.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import com.movilix.torrant.core.RepositoryHelper;
import com.movilix.torrant.core.settings.SettingsRepository;
import com.movilix.torrant.receiver.SchedulerReceiver;

import java.util.Calendar;
import java.util.concurrent.TimeUnit;

import androidx.annotation.NonNull;
import androidx.work.Constraints;
import androidx.work.Data;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.NetworkType;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

public class Scheduler
{
    public static final String SCHEDULER_WORK_START_APP = "scheduler_work_start_app";
    public static final String SCHEDULER_WORK_STOP_APP = "scheduler_work_stop_app";
    public static final String SCHEDULER_WORK_PERIODICAL_REFRESH_FEEDS = "scheduler_work_periodical_refresh_feeds";

    /*
     * Time in minutes after 00:00
     */

    public static void setStartAppAlarm(@NonNull Context appContext, int time)
    {
        setStartStopAppAlarm(appContext, SCHEDULER_WORK_START_APP, time);
    }

    public static void setStopAppAlarm(@NonNull Context appContext, int time)
    {
        setStartStopAppAlarm(appContext, SCHEDULER_WORK_STOP_APP, time);
    }

    public static void cancelStartAppAlarm(@NonNull Context appContext)
    {
        Intent intent = new Intent(appContext, SchedulerReceiver.class);
        PendingIntent pi = PendingIntent.getBroadcast(appContext, SCHEDULER_WORK_START_APP.hashCode(), intent, 0);
        AlarmManager am = (AlarmManager)appContext.getSystemService(Context.ALARM_SERVICE);
        am.cancel(pi);
    }

    public static void cancelStopAppAlarm(@NonNull Context appContext)
    {
        Intent intent = new Intent(appContext, SchedulerReceiver.class);
        PendingIntent pi = PendingIntent.getBroadcast(appContext, SCHEDULER_WORK_STOP_APP.hashCode(), intent, 0);
        AlarmManager am = (AlarmManager)appContext.getSystemService(Context.ALARM_SERVICE);
        am.cancel(pi);
    }

    private static void setStartStopAppAlarm(@NonNull Context appContext,
                                             @NonNull String workTag, int time)
    {
        Calendar calendar = Calendar.getInstance();
        long timeInMillis = System.currentTimeMillis();

        calendar.setTimeInMillis(timeInMillis);
        calendar.set(Calendar.HOUR_OF_DAY, time / 60);
        calendar.set(Calendar.MINUTE, time % 60);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        if (calendar.getTimeInMillis() < timeInMillis + 2000L)
            calendar.add(Calendar.DAY_OF_MONTH, 1);

        Intent intent = new Intent(appContext, SchedulerReceiver.class);
        intent.setAction(workTag);
        PendingIntent pi = PendingIntent.getBroadcast(appContext, workTag.hashCode(), intent, 0);
        AlarmManager am = (AlarmManager)appContext.getSystemService(Context.ALARM_SERVICE);

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M)
            am.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pi);
        else
            am.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pi);
    }

    /*
     * Interval in milliseconds
     */

    public static void runPeriodicalRefreshFeeds(@NonNull Context appContext, long interval)
    {
        Data data = new Data.Builder()
                .putString(FeedFetcherWorker.TAG_ACTION, FeedFetcherWorker.ACTION_FETCH_ALL_CHANNELS)
                .build();
        PeriodicWorkRequest work = new PeriodicWorkRequest.Builder(FeedFetcherWorker.class,
                    interval,
                    TimeUnit.MILLISECONDS)
                .setInputData(data)
                .setConstraints(getRefreshFeedsConstraints(appContext))
                .addTag(SCHEDULER_WORK_PERIODICAL_REFRESH_FEEDS)
                .build();

        WorkManager.getInstance(appContext).enqueueUniquePeriodicWork(SCHEDULER_WORK_PERIODICAL_REFRESH_FEEDS,
                ExistingPeriodicWorkPolicy.REPLACE, work);
    }

    private static Constraints getRefreshFeedsConstraints(Context appContext)
    {
        SettingsRepository pref = RepositoryHelper.getSettingsRepository(appContext);

        NetworkType netType = NetworkType.CONNECTED;
        if (pref.autoRefreshFeedsEnableRoaming())
            netType = NetworkType.NOT_ROAMING;
        if (pref.autoRefreshFeedsUnmeteredConnectionsOnly())
            netType = NetworkType.UNMETERED;

        return new Constraints.Builder()
                .setRequiredNetworkType(netType)
                .build();
    }

    public static void cancelPeriodicalRefreshFeeds(@NonNull Context appContext)
    {
        WorkManager.getInstance(appContext).cancelAllWorkByTag(SCHEDULER_WORK_PERIODICAL_REFRESH_FEEDS);
    }
}
