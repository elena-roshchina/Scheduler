package example.starfox.sheduler;


        import android.annotation.SuppressLint;
        import android.content.Context;
        import android.content.Intent;
        import android.content.SharedPreferences;
        import android.support.annotation.NonNull;
        import android.support.v7.app.AppCompatActivity;
        import android.os.Bundle;
        import android.support.v7.widget.LinearLayoutManager;
        import android.support.v7.widget.RecyclerView;
        import android.view.MenuItem;
        import android.view.View;
        import android.widget.Button;
        import android.widget.TextView;
        import android.widget.Toast;

        import com.google.gson.Gson;
        import com.google.gson.reflect.TypeToken;

        import java.lang.reflect.Type;
        import java.text.DateFormat;
        import java.text.ParseException;
        import java.text.SimpleDateFormat;
        import java.util.ArrayList;
        import java.util.Date;
        import java.util.HashSet;
        import java.util.List;
        import java.util.Set;

        import retrofit2.Call;
        import retrofit2.Callback;
        import retrofit2.Response;
        import android.os.Bundle;
        import android.support.annotation.NonNull;
        import android.support.design.widget.BottomNavigationView;
        import android.support.v7.app.AppCompatActivity;
        import android.view.MenuItem;
        import android.widget.TextView;

public class MainActivity extends AppCompatActivity {


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
    private TextView debugMessage;
    RecyclerView recyclerView;

    //private TextView mTextMessage;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    openScheduleActivity();
                    return true;
                case R.id.navigation_dashboard:
                    openMarksActivity();
                    return true;
                case R.id.navigation_notifications:
                    openMessageActivity();
                    return true;
            }
            return false;
        }
    };

    private void openScheduleActivity(){
        Intent secondActivityIntent = new Intent(this, ScheduleActivity.class);
        startActivity(secondActivityIntent);
    }

    private void openMessageActivity(){
        Intent secondActivityIntent = new Intent(this, MessagesActivity.class);
        startActivity(secondActivityIntent);
    }
    private void openMarksActivity(){
        Intent secondActivityIntent = new Intent(this, MarksActivity.class);
        startActivity(secondActivityIntent);
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Расписание ЦПСМИ");

        TextView mainTitle = findViewById(R.id.main_title);

        String currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());
        mainTitle.setText(currentDateTimeString);
        debugMessage = findViewById(R.id.debug_text);
        debugMessage.setText("Ваши занятия на текущую неделю:");

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        AlarmBroadcastReceiver alarm;

        recyclerView = findViewById(R.id.schedule_recycle_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        //String strDate = "2018-06-22";
        //boolean result = dateInCurrentWeek(strDate);
        //debugMessage.append(strDate + " " + result);

        // / check data in Shared Preferencies
        sharedPref = getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE);
        if (sharedPref.contains(SHARED_SCHEDULE)
                && sharedPref.contains(SHARED_MARKS)){
            Toast.makeText(MainActivity.this, "Schedule found",
                    Toast.LENGTH_SHORT).show();
            Gson gson =  new Gson();
            Type listType = new TypeToken<List<ScheduleModel>>() {}.getType();
            List<ScheduleModel> restoredSchedule
                    = gson.fromJson(sharedPref.getString(SHARED_SCHEDULE,null), listType);
            //select study days of current week
            List<ScheduleModel> selectedScheduleItems = new ArrayList<>();
            selectedScheduleItems.clear();
            for (ScheduleModel item: restoredSchedule){
                if (dateInCurrentWeek(item.getDate())){
                    selectedScheduleItems.add(item);
                }
            }

            // show our data
            ScheduleAdapter adapter = new ScheduleAdapter(selectedScheduleItems);
            recyclerView.setAdapter(adapter);
            recyclerView.getAdapter().notifyDataSetChanged();

            alarm = new AlarmBroadcastReceiver();
            alarm.setAlarm(this);
        } else {
            Toast.makeText(MainActivity.this, "Schedule not foundl",
                    Toast.LENGTH_SHORT).show();
            // Расписания не нашли, проверяем наличие логина в ShPref
            if (sharedPref.contains(SHARED_PREF_LOG) && sharedPref.contains(SHARED_PREF_PASS)){
                String strLogin = sharedPref.getString(SHARED_PREF_LOG,"");
                String strPassword = sharedPref.getString(SHARED_PREF_PASS,"");
                // получить сессию
                App.getAuthApi().getData(strLogin, strPassword)
                        .enqueue(new Callback<IdentificationModel>() {
                            @Override
                            public void onResponse(Call<IdentificationModel> call,
                                                   @NonNull Response<IdentificationModel> response) {
                                if (response.body() != null) {
                                    if (response.body().getStatus()){
                                        Toast.makeText(MainActivity.this,
                                                "LOGIN CORRECT", Toast.LENGTH_SHORT).show();
                                        String session = response.body().getSession();
                                        Context context = getApplicationContext();
                                        //сохранить в shared pref
                                        saveToShared(context,SHARED_PREF_SESSION,session);
                                        // получить расписание
                                        sheduleRequest(context, session);
                                        // получить сообщения
                                        messagesRequest(context, session);
                                        // получить зачетку
                                        marksRequest(context, session);
                                    } else {
                                        Toast.makeText(MainActivity.this,
                                                "LOGIN WRONG", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    Toast.makeText(MainActivity.this, "NO AUTH RESPONSE",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                            @Override
                            public void onFailure(@NonNull Call<IdentificationModel> call, Throwable t) {
                                Toast.makeText(MainActivity.this,"fail auth response",
                                        Toast.LENGTH_SHORT).show();
                            }
                        });
                alarm = new AlarmBroadcastReceiver();
                alarm.setAlarm(this);
            } else {
                // ShPref doesn't contain login, User need to Sign in
                String s = " Please Sign In";
                debugMessage.append(s);
                DialogSignIn signIn = new DialogSignIn();
                signIn.show(getSupportFragmentManager(),"SignIn");
            }// enf of if-else - проверки ShPref на наличие логина
        } // enf of if-else - проверки ShPref на наличие расписания


        final Button signInButton = findViewById(R.id.sign_in_button);
        if (sharedPref.contains(SHARED_PREF_LOG) || sharedPref.contains(SHARED_PREF_PASS)){
            signInButton.setText(R.string.exit);
        } else {
            signInButton.setText(R.string.enter);
        }
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String act = signInButton.getText().toString();
                if (act.equals("sign in")){
                    // считать пароль-логин из диалогового окна
                    //  записать в шаред преференсы
                    DialogSignIn signIn = new DialogSignIn();
                    signIn.show(getSupportFragmentManager(),"SignIn");
                    signInButton.setText(R.string.exit);
                } else {
                    sharedPref = getSharedPreferences(SHARED_PREF,MODE_PRIVATE);
                    editor = sharedPref.edit();
                    editor.remove(SHARED_PREF_LOG);
                    editor.remove(SHARED_PREF_PASS);
                    editor.apply();
                    editor.commit();
                    //debugMessage.append("Log and Pass are removed");
                    signInButton.setText(R.string.enter);
                }
            }
        });
    } // end of onCreate

    private boolean dateInCurrentWeek(String date){
        Date dateNow = new Date();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat format
                = new SimpleDateFormat("yyyy-MM-dd");
        @SuppressLint("SimpleDateFormat") SimpleDateFormat dayOfWeekNow
                = new SimpleDateFormat("E");
        Date dateParsed = null;
        try {
            dateParsed = format.parse(date);
            long difference = dateNow.getTime() - dateParsed.getTime();
            int days = (int)(difference / (24*60*60*1000));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        int dayOfCurrentWeek = 0;
        String numberOfDay = dayOfWeekNow.format(dateNow);
        switch(numberOfDay){
            case "Sun":
                dayOfCurrentWeek = 0;
                break;
            case "Mon":
                dayOfCurrentWeek = 1;
                break;
            case "Tue":
                dayOfCurrentWeek = 2;
                break;
            case "Wed":
                dayOfCurrentWeek = 3;
                break;
            case "Thu":
                dayOfCurrentWeek = 4;
                break;
            case "Fri":
                dayOfCurrentWeek = 5;
                break;
            case "Sat":
                dayOfCurrentWeek = 6;
                break;
        }
        Date startDate = new Date();
        startDate.setTime(dateNow.getTime() - dayOfCurrentWeek*24*60*60*1000);
        Date endDate = new Date();
        endDate.setTime(startDate.getTime() + 6*24*60*60*1000);
        if (dateParsed != null
                && (dateParsed.after(startDate))
                && (dateParsed.before(endDate))) {
            return true;
        } else {
            return false;
        }
    } // end of dateInCurrentWeek

    // записывает в SHARED_PREF id последнего commit
    private void lastUpdateIDRequest(String session){
        App.getLastUpdateIDApi().getData(session).enqueue(new Callback<LastUpdateIDModel>() {
            @Override
            public void onResponse(@NonNull Call<LastUpdateIDModel> call, Response<LastUpdateIDModel> response) {
                if (response.body() != null) {
                    String lastCommitID = response.body().getCommitId();
                    saveToShared(getApplicationContext(),SHARED_LAST_UPDATE,lastCommitID);
                } else {
                    String s = "LAST UPDATE RESPONSE NULL";
                    Toast.makeText(MainActivity.this, s, Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<LastUpdateIDModel> call, Throwable t) {
                String s = "LAST UPDATE FAIL";
                Toast.makeText(MainActivity.this, s, Toast.LENGTH_SHORT).show();
            }
        });
    }    // end of lastUpdateIDRequest


    // записывает в SHARED_PREF_SESSION  номер сессии
    private void authRequest(String login, String password){
        App.getAuthApi().getData(login,password)
                .enqueue(new Callback<IdentificationModel>() {
                    @Override
                    public void onResponse(Call<IdentificationModel> call, Response<IdentificationModel> response) {
                        if (response.body() != null)  {
                            if (response.body().getStatus()){
                                String s = "PSWD CORRECT " + response.body().getSession();
                                Toast.makeText(MainActivity.this, s, Toast.LENGTH_SHORT).show();
                                String session = response.body().getSession();
                                saveToShared(getApplicationContext(),SHARED_PREF_SESSION,session);
                            } else {
                                String s = "PASSWORD WRONG";
                                Toast.makeText(MainActivity.this, s, Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            String s = "No response";
                            Toast.makeText(MainActivity.this, s, Toast.LENGTH_SHORT).show();
                        }
                    }
                    @Override
                    public void onFailure(Call<IdentificationModel> call, Throwable t) {
                        Toast.makeText(MainActivity.this,"Sorry, no response",
                                Toast.LENGTH_SHORT).show();
                    }
                });
    } // end of authRequest


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
                } else {
                    Toast.makeText(context,"MARK RESPONSE EMPTY",
                            Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<MarksModel> call, Throwable t) {
                Toast.makeText(context,"MARK RESPONSE FAIL",
                        Toast.LENGTH_SHORT).show();
            }
        });
    } // end of marksRequest

    // загружает и сохраняет сообщения
    private void messagesRequest(final Context context, String session){
        App.getMessagesApi().getData(session).enqueue(new Callback<MessagesModel>() {
            @Override
            public void onResponse(Call<MessagesModel> call, Response<MessagesModel> response) {
                if (response.body() != null){
                    Toast.makeText(context,
                            "GOT MESSAGES", Toast.LENGTH_SHORT).show();
                    List<MsgList> messages =  new ArrayList<>();
                    messages.addAll(response.body().getMsgList());
                    Gson gson =  new Gson();
                    String msgJson = gson.toJson(messages);
                    saveToShared(context, SHARED_MESSAGES, msgJson);
                }
            }
            @Override
            public void onFailure(Call<MessagesModel> call, Throwable t) {
                Toast.makeText(context,"MSG RESPONSE FAIL",
                        Toast.LENGTH_SHORT).show();
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
                            Toast.makeText(MainActivity.this,
                                    "GOT SCHEDULE", Toast.LENGTH_SHORT).show();
                            List<ScheduleModel> schedule = new ArrayList<>();
                            schedule.addAll(response.body());
                            Gson gson =  new Gson();
                            String schedJson = gson.toJson(schedule);
                            saveToShared(context, SHARED_SCHEDULE, schedJson);
                            //select study days for current week to show
                            List<ScheduleModel> selectedScheduleItems = new ArrayList<>();
                            selectedScheduleItems.clear();
                            for (ScheduleModel item: schedule){
                                if (dateInCurrentWeek(item.getDate())){
                                    selectedScheduleItems.add(item);
                                }
                            }
                            // show schedule
                            ScheduleAdapter adapter = new ScheduleAdapter(selectedScheduleItems);
                            recyclerView.setAdapter(adapter);
                            recyclerView.getAdapter().notifyDataSetChanged();
                        }
                    }
                    @Override
                    public void onFailure(Call<List<ScheduleModel>> call, Throwable t) {
                        Toast.makeText(context, "GET SCHEDULE FAILED", Toast.LENGTH_SHORT).show();
                    }
                });
    } // end of scheduleRequest

    private void saveToShared(Context context, String key, String savedString) {
        sharedPref = context.getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE);
        editor = sharedPref.edit();
        editor.putString(key, savedString);
        editor.apply();
        editor.commit();
        if (sharedPref.contains(key)){
            Toast.makeText(context,key + " SAVED", Toast.LENGTH_SHORT).show();
        }
    }
}