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

import com.google.gson.Gson;

import java.io.IOException;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

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
    String session;

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
                public void onResponse(@NonNull Call<IdentificationModel> call, Response<IdentificationModel> response) {
                    if (response.body() != null) {
                        //msgStr.append("Auth ");
                        //msgStr.append(response.body().getStatus());
                        session = response.body().getSession();
                        App.getLastUpdateIDApi().getData(session).enqueue(new Callback<LastUpdateIDModel>() {
                            @Override
                            public void onResponse(@NonNull Call<LastUpdateIDModel> call, Response<LastUpdateIDModel> response) {
                                if (response.body() != null) {
                                    //msgStr.append(" ID ");
                                    String lastCommitID = response.body().getCommitId();
                                    //msgStr.append(lastCommitID);
                                    sharedPref = context.getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE);
                                    //if (sharedPref.contains(SHARED_LAST_UPDATE)){
                                        //if (!sharedPref.getString(SHARED_LAST_UPDATE,"").equals(lastCommitID)){
                                            sheduleRequest(context,session);
                                            marksRequest(context,session);
                                            messagesRequest(context,session);
                                            msgStr.append(" Schedule updated ");
                                            letCreateNotification(context, msgStr.toString());
                                        //}
                                    //}
                                }
                            }
                            @Override
                            public void onFailure(Call<LastUpdateIDModel> call, Throwable t) {
                                msgStr.append("ID fail");
                            }
                        });
                    } else {
                        msgStr.append("Response NULL ");
                    }
                    Format formatter = new SimpleDateFormat("hh:mm:ss a");
                    msgStr.append(formatter.format(new Date()));
                    letCreateNotification(context, msgStr.toString());
                }

                @Override
                public void onFailure(Call<IdentificationModel> call, Throwable t) {
                    msgStr.append("Auth fail");
                    Format formatter = new SimpleDateFormat("hh:mm:ss a");
                    msgStr.append(formatter.format(new Date()));
                    letCreateNotification(context, msgStr.toString());
                }
            });

        } else {
            msgStr.append("No Login found at ");
            Format formatter = new SimpleDateFormat("hh:mm:ss a");
            msgStr.append(formatter.format(new Date()));
            letCreateNotification(context, msgStr.toString());
        }

        //Разблокируем поток.
        wl.release();
    }

    private void letCreateNotification(Context context, String ourMessage){
        Intent resultIntent = new Intent(context, MessagesActivity.class);
        PendingIntent resultPendingIntent = PendingIntent.getActivity(context, 0, resultIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        Notification notification = createNotification(context, resultPendingIntent,
                R.mipmap.ic_launcher,
                "ЦПСМИ ", ourMessage);
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
            /*am.setRepeating(AlarmManager.RTC_WAKEUP,
                    System.currentTimeMillis(),
                    60*60*1000L,
                    pendingIntent); */
            am.setInexactRepeating(AlarmManager.RTC_WAKEUP,
                    System.currentTimeMillis(),
                    AlarmManager.INTERVAL_HOUR,
                    pendingIntent);
            Toast.makeText(context, "set alarm" , Toast.LENGTH_SHORT).show();
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

    // записывает в SHARED_PREF id последнего commit
    private void lastUpdateIDRequest(final Context context, final String session){
        App.getLastUpdateIDApi().getData(session).enqueue(new Callback<LastUpdateIDModel>() {
            @Override
            public void onResponse(@NonNull Call<LastUpdateIDModel> call, Response<LastUpdateIDModel> response) {
                if (response.body() != null) {
                    String lastCommitID = response.body().getCommitId();
                    sharedPref = context.getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE);
                    if (sharedPref.contains(SHARED_LAST_UPDATE)){
                        if (!sharedPref.getString(SHARED_LAST_UPDATE,"").equals(lastCommitID)){
                            sheduleRequest(context,session);
                            marksRequest(context,session);
                            messagesRequest(context,session);
                            @SuppressLint("SimpleDateFormat") Format formatter = new SimpleDateFormat("hh:mm:ss a");
                            msgStr.append(formatter.format(new Date()));
                            msgStr.append("UDTATED");
                            letCreateNotification(context, msgStr.toString());
                        }
                    } else {
                        saveToShared(context,SHARED_LAST_UPDATE,lastCommitID);
                    }
                }
            }
            @Override
            public void onFailure(Call<LastUpdateIDModel> call, Throwable t) {
            }
        });
    }    // end of lastUpdateIDRequest

    // загружает и сохраняет оценки
    private void marksRequest(final Context context, String session){
        App.getMarksApi().getData(session).enqueue(new Callback<MarksModel>() {
            @Override
            public void onResponse(Call<MarksModel> call, Response<MarksModel> response) {
                if (response.body() != null){
                    Toast.makeText(context,
                            "GOT MARKS", Toast.LENGTH_SHORT).show();
                    List<SubjectsList> marks =  new ArrayList<>();
                    marks.addAll(response.body().getSubjectsList());
                    Gson gson =  new Gson();
                    String marksJson = gson.toJson(marks);
                    saveToShared(context, SHARED_MARKS, marksJson);
                }
            }
            @Override
            public void onFailure(Call<MarksModel> call, Throwable t) {

            }
        });
    } // end of marksRequest

    // загружает и сохраняет сообщения
    private void messagesRequest(final Context context, String session){
        App.getMessagesApi().getData(session).enqueue(new Callback<MessagesModel>() {
            @Override
            public void onResponse(Call<MessagesModel> call, Response<MessagesModel> response) {
                if (response.body() != null){
                    List<MsgList> messages =  new ArrayList<>();
                    messages.addAll(response.body().getMsgList());
                    Gson gson =  new Gson();
                    String msgJson = gson.toJson(messages);
                    saveToShared(context, SHARED_MESSAGES, msgJson);
                }
            }
            @Override
            public void onFailure(Call<MessagesModel> call, Throwable t) {
            }
        });
    } // end of messageRequest

    // загружает и сохраняет расписание
    public void sheduleRequest(final Context context, String session){
        App.getScheduleApi().getData(session)
                .enqueue(new Callback<List<ScheduleModel>>() {
                    @Override
                    public void onResponse(Call<List<ScheduleModel>> call,
                                           Response<List<ScheduleModel>> response) {
                        if (response.body() != null){
                            List<ScheduleModel> schedule = new ArrayList<>();
                            schedule.addAll(response.body());
                            Gson gson =  new Gson();
                            String schedJson = gson.toJson(schedule);
                            saveToShared(context, SHARED_SCHEDULE, schedJson);
                        }
                    }
                    @Override
                    public void onFailure(Call<List<ScheduleModel>> call, Throwable t) {

                    }
                });
    } // end of scheduleRequest

    private void saveToShared(Context context, String key, String savedString) {
        sharedPref = context.getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE);
        editor = sharedPref.edit();
        editor.putString(key, savedString);
        editor.apply();
        editor.commit();
    }
}

