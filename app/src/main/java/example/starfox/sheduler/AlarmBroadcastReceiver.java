package example.starfox.sheduler;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

import java.io.IOException;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class AlarmBroadcastReceiver extends BroadcastReceiver {

    private static final String SHARED_PREF = "MY_SHARED_PREF";
    private static final String SHARED_PREF_LOG = "USER_LOGIN";
    private static final String SHARED_PREF_PASS = "USER_PASS";
    private static final String SHARED_PREF_SESSION = "SESSION";
    private static final String SHARED_SCHEDULE = "SCHEDULE";
    private static final String SHARED_MARKS = "MARKS";
    private static final String SHARED_MESSAGES = "MESSAGES";
    private static final String SHARED_LAST_UPDATE = "LAST_UPDATE";
    private SharedPreferences sharedPref;
    private SharedPreferences.Editor editor;
    StringBuilder msgStr;

    @Override
    public void onReceive(final Context context, Intent intent) {
        PowerManager pm=(PowerManager) context.getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock wl= null;
        if (pm != null) {
            wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,"YOUR TAG");
        }
        //Осуществляем блокировку
        assert wl != null;
        wl.acquire(10*60*1000L /*10 minutes*/);
        msgStr = new StringBuilder();
        // Check ShPref
        sharedPref = context.getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE);
        if (sharedPref.contains(SHARED_PREF_LOG) && sharedPref.contains(SHARED_PREF_PASS)){
           // msgStr.append("Login found at ");
            String strLogin = sharedPref.getString(SHARED_PREF_LOG,"");
            String strPassword = sharedPref.getString(SHARED_PREF_PASS,"");
            App.getAuthApi().getData(strLogin, strPassword).enqueue(new Callback<IdentificationModel>() {
                @Override
                public void onResponse(Call<IdentificationModel> call, Response<IdentificationModel> response) {
                    if (response.body() != null) {
                        msgStr.append("Response " + response.body().getStatus());
                    } else {
                        msgStr.append("Response NULL");
                    }
                    Format formatter = new SimpleDateFormat("hh:mm:ss a");
                    msgStr.append(formatter.format(new Date()));
                    letCreateNotification(context, msgStr.toString());
                }

                @Override
                public void onFailure(Call<IdentificationModel> call, Throwable t) {
                    msgStr.append("Response FAIL");
                    Format formatter = new SimpleDateFormat("hh:mm:ss a");
                    msgStr.append(formatter.format(new Date()));
                    letCreateNotification(context, msgStr.toString());
                }
            });

        } else {
            msgStr.append("No Login found at ");
            letCreateNotification(context, msgStr.toString());
        }

        //msgStr.append("WAKE UP ");


        /*
        Toast.makeText(context, msgStr, Toast.LENGTH_LONG).show();
        */
        //Создаем Intent для запуска Activity и упаковываем его в PedningIntent
        // по клику на уведомление запустится mainactivity
        /*
        Intent resultIntent = new Intent(context, MainActivity.class);
        PendingIntent resultPendingIntent = PendingIntent.getActivity(context, 0, resultIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        Notification notification = createNotification(context, resultPendingIntent,
                R.mipmap.ic_launcher,
                "Notification ", msgStr.toString());
        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (notificationManager != null) {
            notificationManager.notify(1, notification);
        }
        */
        //Разблокируем поток.
        wl.release();
    }

    private void letCreateNotification(Context context, String ourMessage){
        Intent resultIntent = new Intent(context, MainActivity.class);
        PendingIntent resultPendingIntent = PendingIntent.getActivity(context, 0, resultIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        Notification notification = createNotification(context, resultPendingIntent,
                R.mipmap.ic_launcher,
                "Notification ", ourMessage);
        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (notificationManager != null) {
            notificationManager.notify(1, notification);
        }
    } // end of letCreateNotification

    public void setAlarm(Context context)
    {
        AlarmManager am = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmBroadcastReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0);
        if (am != null) {
            am.setRepeating(AlarmManager.RTC_WAKEUP,
                    System.currentTimeMillis(),
                    60*60*1000L,
                    pendingIntent);
            Toast.makeText(context, "set alarm ", Toast.LENGTH_SHORT).show();
        }
    }

    public void cancelAlarm(Context context)
    {
        Intent intent = new Intent(context, AlarmBroadcastReceiver.class);
    }

    private Notification createNotification(Context context, PendingIntent resultPendingIntent,
                                            int icon, String title, String txt){
        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(icon)
                        .setContentTitle(title)
                        .setContentText(txt)
                        .setContentIntent(resultPendingIntent);
        return builder.build();
    }



}

