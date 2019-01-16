package com.programasoft.drinkshopserver.Utils;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.ContextWrapper;
import android.net.Uri;
import android.os.Build;

public class NotificationHelper extends ContextWrapper {

    private static final String  DRINK_SHOP_CHANNEL_ID="com.programasoft.drinkshopserver.PROGRAMASOFT";
    private static final String  DRINK_SHOP_CHANNEL_NAME="Drink Shop";
    private NotificationManager notificationManager;

    public NotificationHelper(Context base) {
        super(base);
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O)
        {CreateChannel();
        }

    }
    @TargetApi(Build.VERSION_CODES.O)
    private void CreateChannel() {
        NotificationChannel channel=new NotificationChannel(DRINK_SHOP_CHANNEL_ID,DRINK_SHOP_CHANNEL_NAME,NotificationManager.IMPORTANCE_DEFAULT);
        channel.enableLights(false);
        channel.enableVibration(true);
        channel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
        getNotificationManager().createNotificationChannel(channel);
    }

    public NotificationManager getNotificationManager() {
       if(notificationManager!=null)
       {
           notificationManager=(NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
       }

       return notificationManager;
    }

    @TargetApi(Build.VERSION_CODES.O)
    public Notification.Builder GetDrinkShopNotification(String title,String message,Uri SoundUri) {
        Notification.Builder builder=new Notification.Builder(this,DRINK_SHOP_CHANNEL_ID).
                setContentText(message).
                setContentTitle(title).setSound(SoundUri).setAutoCancel(true);
        return builder;
    }
}
