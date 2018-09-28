package com.example.android.unipin;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.os.Build;
import android.support.v4.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private String image;
    private String title;
    private String body;
    private String sound;
    private int id;
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        title = remoteMessage.getNotification().getTitle();
        image = remoteMessage.getNotification().getIcon();
        body = remoteMessage.getNotification().getBody();
        sound = remoteMessage.getNotification().getSound();
        id = 0;
        Object obj = remoteMessage.getData().get("id");
        if (obj != null) {
            id = Integer.valueOf(obj.toString());
        }
    }

    private void sendNotification(){
        Intent intent = new Intent(this,MainActivity.class);
        intent.putExtra("text",body);

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,0,intent
        ,PendingIntent.FLAG_ONE_SHOT);

        NotificationCompat.Builder notificationBuilder = null;
        try {

            notificationBuilder = new NotificationCompat.Builder(this)
                    .setContentTitle(URLDecoder.decode(title,"UTF-8"))
                    .setContentText(URLDecoder.decode(body,"UTF-8"))
                    .setAutoCancel(true)
                    .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                    .setContentIntent(pendingIntent);
        }
        catch (UnsupportedEncodingException e){
            e.printStackTrace();
        }
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("Default",
                    "Default channel", NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }
        if(notificationBuilder!=null){
            notificationManager.notify(id,notificationBuilder.build());
        }
    }

}
