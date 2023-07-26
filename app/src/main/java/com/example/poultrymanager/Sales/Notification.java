package com.example.poultrymanager.Sales;

import android.content.Context;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.poultrymanager.R;

public class Notification extends NotificationCompat {
    Context context;
    String channelID;
    CharSequence contentText;

    public Notification(Context context, String channelID, CharSequence contentText) {
        this.context = context;
        this.channelID = channelID;
        this.contentText = contentText;

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context,channelID)
                .setSmallIcon(R.drawable.ic_egg)
                .setContentTitle("Poultry Manager")
                .setContentText(contentText)
                .setAutoCancel(true)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setPriority(NotificationCompat.PRIORITY_HIGH);

        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);
        notificationManagerCompat.notify(1, builder.build());
    }
}
