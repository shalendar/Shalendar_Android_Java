package kr.ac.smu.cs.shalendar_java;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

public class MyFireBaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "FMS";

    public MyFireBaseMessagingService() {

    }

    @Override
    public void onNewToken(String token) {
        super.onNewToken(token);
        Log.d(TAG, "newToken() called: " + token);
    }


    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        //data payload가 있으면 foreground
        if(remoteMessage.getData().size() > 0) {
            sendNotification(remoteMessage.getData());
        }


        //data notification이면 background
        if(remoteMessage.getNotification() != null) {
            sendNotification(remoteMessage.getNotification().getBody());
        }
    }



    //ForeGround 처리
    private void sendNotification(Map<String, String> data) {

        Intent intent = new Intent(this, WaitInvite.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        //push로 받은 data를 그대로 다시 intent에 넣는다.
        if(data != null && data.size() > 0) {
            for(String key:data.keySet()) {
                intent.putExtra(key, data.get(key));
            }
        }

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
        String channelID = getString(R.string.default_datapayload_channel_id);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this, channelID)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle("Shalendar")
                        .setAutoCancel(true)
                        .setSound(defaultSoundUri)
                        .setContentIntent(pendingIntent);

        NotificationManager notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String channelName = getString(R.string.default_datapayload_channel_id);
            NotificationChannel channel = new NotificationChannel(channelID, channelName, NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(channel);

        }

        notificationManager.notify(0, notificationBuilder.build());
    }

    //backGround 처리
    private void sendNotification(String messageBody) {

        Intent intent = new Intent(this, WaitInvite.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);


        String channelID = getString(R.string.default_notification_channel_id);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this, channelID)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("Shalendar")
                .setContentText(messageBody)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String channelName = getString(R.string.default_notification_channel_id);
            NotificationChannel channel = new NotificationChannel(channelID, channelName, NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(channel);

        }

        notificationManager.notify(0, notificationBuilder.build());
    }
}
