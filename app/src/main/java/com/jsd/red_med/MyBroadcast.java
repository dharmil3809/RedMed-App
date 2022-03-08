package com.jsd.red_med;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Vibrator;

public class MyBroadcast extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Vibrator vibrator=(Vibrator)context.getSystemService(context.VIBRATOR_SERVICE);
        vibrator.vibrate(2000);

        Notification noti = new Notification.Builder(context)
                .setContentTitle("Alarm is ON")
                .setContentText("You had to  set up the alarm")
                .setSmallIcon(R.mipmap.ic_launcher).build();


        NotificationManager manager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        noti.flags|= Notification.FLAG_AUTO_CANCEL;
        manager.notify(0,noti);


        Uri notificatons = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);

        Ringtone r = RingtoneManager.getRingtone(context,notificatons);
        r.play();
    }
}
