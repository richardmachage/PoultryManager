package com.example.poultrymanager;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.poultrymanager.Eggs.EggsFragment;

public class EggDataReminder extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i("Broadcast","BroadCastReceiver Fired");

        Intent notificationIntent = new Intent(context, MainActivity.class);
        notificationIntent.putExtra("FRAGMENT_DESTINATION","eggFragment");
        notificationIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        notificationIntent.setAction(Intent.ACTION_MAIN);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(context,0,notificationIntent,0);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context,"eggReminder")
                .setSmallIcon(R.drawable.ic_egg)
                .setContentTitle("Poultry Manager")
                .setContentText("Egg collection Reminder,\nyou have not recorded eggs collected Today")
                .setAutoCancel(true)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(pendingIntent);

        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);
        notificationManagerCompat.notify(123, builder.build());
    }

}

