package com.project.dailyselfie;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.util.Log;

import java.text.DateFormat;
import java.util.Date;

public class NotificationReceiver extends BroadcastReceiver {

    private static final int MY_NOTIFICATION_ID = 1;
    private static final String TAG = "NotificationReceiver";

    private Intent mNotificationIntent;
    private PendingIntent mContentIntent;

    private Uri soundURI = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
    private long[] mVibratePattern = { 0, 200, 200, 300 };

    @Override
    public void onReceive(Context context, Intent intent) {

        // Intent to be used when the user clicks on the Notification View
        mNotificationIntent = new Intent(context, MainActivity.class);

        // PendingIntent that wraps the underlying Intent
        mContentIntent = PendingIntent.getActivity(context, 0,
                mNotificationIntent, Intent.FLAG_ACTIVITY_NEW_TASK);

        // Build the Notification
        Notification.Builder notificationBuilder = new Notification.Builder(
                context).setTicker(context.getString(R.string.notification_ticker_text))
                .setSmallIcon(android.R.mipmap.sym_def_app_icon)
                .setAutoCancel(true).setContentTitle(context.getString(R.string.app_name))
                .setContentText(context.getString(R.string.notification_content_text)).setContentIntent(mContentIntent)
                .setSound(soundURI).setVibrate(mVibratePattern);

        // Get the NotificationManager
        NotificationManager mNotificationManager = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);

        // Pass the Notification to the NotificationManager:
        mNotificationManager.notify(MY_NOTIFICATION_ID,
                notificationBuilder.build());

        Log.i(TAG, "Sending notification at:" + DateFormat.getDateTimeInstance().format(new Date()));
	}
}