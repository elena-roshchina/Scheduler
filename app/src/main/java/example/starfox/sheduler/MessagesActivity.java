package example.starfox.sheduler;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public class MessagesActivity extends AppCompatActivity {
    private static final String SHARED_PREF = "MY_SHARED_PREF";
    private static final String SHARED_MESSAGES = "MESSAGES";

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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messages);
        setTitle("Сообщения");

        SharedPreferences sharedPref;
        RecyclerView recyclerView;

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        sharedPref = getSharedPreferences(SHARED_PREF, MODE_PRIVATE);
        if (sharedPref.contains(SHARED_MESSAGES)){

            Gson gson =  new Gson();
            Type listType = new TypeToken<List<MsgList>>() {}.getType();
            String msg = sharedPref.getString(SHARED_MESSAGES,"");
            List<MsgList> restoredMessages = gson.fromJson(msg,listType);
            int n = restoredMessages.size();
            restoredMessages.add(setEmptyMessage(" "));
            Toast.makeText(this, n + " Messages found",
                    Toast.LENGTH_SHORT).show();
            // show our data
            recyclerView = findViewById(R.id.messages_recycle_view);
            LinearLayoutManager layoutManager = new LinearLayoutManager(this);
            recyclerView.setLayoutManager(layoutManager);
            MessagesAdapter adapter = new MessagesAdapter(restoredMessages);
            recyclerView.setAdapter(adapter);
            recyclerView.getAdapter().notifyDataSetChanged();
        }
    }
    private MsgList setEmptyMessage(String s){
        MsgList lastMessage = new MsgList();
        lastMessage.setDstId(s);
        lastMessage.setDstType(s);
        lastMessage.setMsg(s);
        lastMessage.setId(s);
        lastMessage.setStamp(s);
        return lastMessage;
    }
}
