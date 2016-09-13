package com.example.delevere.cbook;

import android.annotation.TargetApi;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;

/**
 * Created by Delevere on 21-Jul-16.
 */
public class AlarmReceiver extends BroadcastReceiver {

    static int RQS_1 = 1;
    private DataBaseHelper db;
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onReceive(Context context, Intent intent) {
        //Toast.makeText(context, "Cbook Event Alert", Toast.LENGTH_LONG).show();
        Uri alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        if (alarmUri == null) {
            alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            if (alarmUri == null) {
                // alert backup is null, using 2nd backup
                alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
            }
        }
        //Ringtone ringtone = RingtoneManager.getRingtone(context, alarmUri);
        //ringtone.play();

        //Intent intent1 = new Intent(context, EventActivity.class);
        //TaskStackBuilder stackBuilder1 = TaskStackBuilder.create(context);
        //stackBuilder1.addParentStack(EventActivity.class);
        //stackBuilder1.addNextIntent(intent1);
        //PendingIntent resultPendingIntent1 = stackBuilder1.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        String title = intent.getStringExtra("EVENT");
        String subtitle = intent.getStringExtra("PERSON");
        String id = intent.getStringExtra("key id");

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle(title)
                        .setSound(alarmUri)
                                .setAutoCancel(true)
                        //.addAction(R.mipmap.ic_launcher,"stop alarm",p)
                        .setContentText(subtitle);
        Intent resultIntent = new Intent(context, Home2Activicy.class);
        //resultIntent.putExtra("key id",'8');
        //intent.putExtra("key id", id);
        //intent.putExtra("key 2", subtitle);
        //intent.putExtra("key 3", title);
        //intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);


        //Intent resultIntent = new Intent(context, LoginActivity.class);
        //TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        //stackBuilder.addParentStack(EventActivity.class);
        //stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent = PendingIntent.getActivity(context,0,resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        mBuilder.setContentIntent(resultPendingIntent);
        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(RQS_1, mBuilder.build());
        RQS_1 += 1;

    }


}
