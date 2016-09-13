package com.example.delevere.cbook;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;

import com.google.android.gms.gcm.GcmListenerService;


/**
 * Created by Belal on 4/15/2016.
 */

//Class is extending GcmListenerService
public class GCMPushReceiverService extends GcmListenerService {
    static int RQS_1 = 1;
    private DataBaseHelper db = new DataBaseHelper(this);
    //This method will be called on every new message received
    @Override
    public void onMessageReceived(String from, Bundle data) {
        //Getting the message from the bundle


        String number = data.getString("event_person");
        String type = data.getString("message");
        String date = data.getString("event_time");
        String image = data.getString("event_image");
        String created = data.getString("event_created");

        Cursor cu = db.getContactName(number);

        String title = cu.getString(cu.getColumnIndexOrThrow("name"));
        //Displaying a notiffication with the message
        db.inserEvent(title,number ,type, date,image,created);


        sendNotification(type,title);

    }

    //This method is generating a notification and displaying the notification
    private void sendNotification(String message,String title) {
        Intent intent = new Intent(this, Home2Activicy.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        int requestCode = 0;
        PendingIntent pendingIntent = PendingIntent.getActivity(this, requestCode, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        Uri sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder noBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(title)
                .setContentText(message)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(RQS_1, noBuilder.build()); //0 = ID of notification
        RQS_1 += 1;
    }
}