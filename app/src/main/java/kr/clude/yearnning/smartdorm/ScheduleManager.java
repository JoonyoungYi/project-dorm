package kr.clude.yearnning.smartdorm;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.SystemClock;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


/**
 * Created by yearnning on 15. 1. 23..
 */
public class ScheduleManager extends BroadcastReceiver {

    final public static String ONE_TIME = "onetime";
    final public static String TITLE = "title";
    final public static String ID = "id";
    private Context mContext;

    @Override
    public void onReceive(Context context, Intent intent) {

        this.mContext = context;

        /**
         * //Acquire the lock
         */
        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "KAIST_BAB_ScheduleManager");
        wl.acquire();

        /**
         * You can do the processing here.
         */
        Bundle extras = intent.getExtras();
        StringBuilder msgStr = new StringBuilder();

        if (extras != null && extras.getBoolean(ONE_TIME, Boolean.FALSE)) {
            //Make sure this intent has been sent by the one-time timer button.
            msgStr.append("One time Timer : ");
        }
        Format formatter = new SimpleDateFormat("hh:mm:ss a");
        msgStr.append(formatter.format(new Date()));

        /**
         *
         */
        String title = extras.getString(TITLE);
        createNotification(title);

        /**
         * Release the lock
         */
        wl.release();
    }

    private void createNotification(String title) {

        /**
         * Build intent for notification content
         */
        int notificationId = 001;
        Intent intent = new Intent(mContext, MainActivity.class);
        PendingIntent viewPendingIntent =
                PendingIntent.getActivity(mContext, 0, intent, PendingIntent.FLAG_ONE_SHOT);

        /**
         * Build
         */
        String content = "현재 비어있습니다. 지금 가야 이득";
        NotificationCompat.BigTextStyle bigStyle =
                new NotificationCompat.BigTextStyle()
                        .setBigContentTitle(title)
                        .bigText(content);

        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(mContext)
                        .setSmallIcon(R.drawable.ic_launcher)
                        .setContentTitle(title)
                        .setContentText(content)
                        .setContentIntent(viewPendingIntent)
                        .setStyle(bigStyle)
                        .setAutoCancel(true);

        /**
         *  Get an instance of the NotificationManager service
         */
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(mContext);

        /**
         * Build the notification and issues it with notification manager.
         */
        notificationManager.notify(notificationId, builder.build());

    }

    /**
     * @param context
     */
    public void SetAlarm(Context context, final int id, final String title, int time_offset) {
        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, ScheduleManager.class);
        intent.putExtra(ONE_TIME, Boolean.FALSE);
        intent.putExtra(TITLE, title);
        PendingIntent pi = PendingIntent.getBroadcast(context, id, intent, 0);
        //After after 5 seconds
        am.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + time_offset * 1000, pi);
    }

    /**
     * @param context
     */
    public void CancelAlarm(Context context, int id) {
        Intent intent = new Intent(context, ScheduleManager.class);
        PendingIntent sender = PendingIntent.getBroadcast(context, id, intent, 0);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(sender);
    }


}