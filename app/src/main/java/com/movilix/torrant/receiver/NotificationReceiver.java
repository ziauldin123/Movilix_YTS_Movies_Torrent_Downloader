

package com.movilix.torrant.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.movilix.torrant.service.TorrentService;
import com.movilix.MainActivity;

/*
 * The receiver for actions of foreground notification, added by service.
 */

public class NotificationReceiver extends BroadcastReceiver
{
    public static final String NOTIFY_ACTION_SHUTDOWN_APP = "com.movilix.torrant.receivers.NotificationReceiver.NOTIFY_ACTION_SHUTDOWN_APP";
    public static final String NOTIFY_ACTION_ADD_TORRENT = "com.movilix.torrant.receivers.NotificationReceiver.NOTIFY_ACTION_ADD_TORRENT";
    public static final String NOTIFY_ACTION_PAUSE_ALL = "com.movilix.torrant.receivers.NotificationReceiver.NOTIFY_ACTION_PAUSE_ALL";
    public static final String NOTIFY_ACTION_RESUME_ALL = "com.movilix.torrant.receivers.NotificationReceiver.NOTIFY_ACTION_RESUME_ALL";

    @Override
    public void onReceive(Context context, Intent intent)
    {
        String action = intent.getAction();
        if (action == null)
            return;
        Intent mainIntent, serviceIntent;
        switch (action) {
            /* Send action to the already running service */
            case NOTIFY_ACTION_SHUTDOWN_APP:
                mainIntent = new Intent(context.getApplicationContext(), MainActivity.class);
                mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                mainIntent.setAction(NOTIFY_ACTION_SHUTDOWN_APP);
                context.startActivity(mainIntent);


                serviceIntent = new Intent(context.getApplicationContext(), TorrentService.class);
                serviceIntent.setAction(NOTIFY_ACTION_SHUTDOWN_APP);
                context.startService(serviceIntent);
                break;
            case NOTIFY_ACTION_ADD_TORRENT:
                mainIntent = new Intent(context.getApplicationContext(), MainActivity.class);
                mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                mainIntent.setAction(NOTIFY_ACTION_ADD_TORRENT);
                context.startActivity(mainIntent);
                break;
            case NOTIFY_ACTION_PAUSE_ALL:
                serviceIntent = new Intent(context.getApplicationContext(), TorrentService.class);
                serviceIntent.setAction(NOTIFY_ACTION_PAUSE_ALL);
                context.startService(serviceIntent);
                break;
            case NOTIFY_ACTION_RESUME_ALL:
                serviceIntent = new Intent(context.getApplicationContext(), TorrentService.class);
                serviceIntent.setAction(NOTIFY_ACTION_RESUME_ALL);
                context.startService(serviceIntent);
                break;
        }
    }
}
