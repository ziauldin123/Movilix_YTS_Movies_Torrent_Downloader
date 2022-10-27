

package com.movilix.torrant.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.movilix.torrant.core.RepositoryHelper;
import com.movilix.torrant.core.model.TorrentEngine;
import com.movilix.torrant.core.settings.SettingsRepository;
import com.movilix.torrant.service.Scheduler;

/*
 * The receiver for autostart service.
 */

public class BootReceiver extends BroadcastReceiver
{
    @Override
    public void onReceive(Context context, Intent intent)
    {
        if (intent.getAction() == null)
            return;

        Context appContext = context.getApplicationContext();
        SettingsRepository pref = RepositoryHelper.getSettingsRepository(appContext);

        if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) {
            initScheduling(context, pref);

            if (pref.autostart() && pref.keepAlive())
                TorrentEngine.getInstance(appContext).start();
        }
    }

    private void initScheduling(Context appContext, SettingsRepository pref)
    {
        if (pref.enableSchedulingStart())
            Scheduler.setStartAppAlarm(appContext, pref.schedulingStartTime());

        if (pref.enableSchedulingShutdown())
            Scheduler.setStopAppAlarm(appContext, pref.schedulingShutdownTime());
    }
}
