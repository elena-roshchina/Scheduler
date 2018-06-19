package example.starfox.sheduler;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public class MessagesActivity extends AppCompatActivity {
    private static final String SHARED_PREF = "MY_SHARED_PREF";
    private static final String SHARED_PREF_LOG = "USER_LOGIN";
    private static final String SHARED_PREF_PASS = "USER_PASS";
    private static final String SHARED_PREF_SESSION = "SESSION";
    private static final String SHARED_SCHEDULE = "SCHEDULE";
    private static final String SHARED_MARKS = "MARKS";
    private static final String SHARED_MESSAGES = "MESSAGES";
    private static final String SHARED_LAST_UPDATE = "LAST_UPDATE";
    private SharedPreferences sharedPref;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messages);
        setTitle("Сообщения");

        sharedPref = getSharedPreferences(SHARED_PREF, MODE_PRIVATE);
        if (sharedPref.contains(SHARED_MESSAGES)){

            Gson gson =  new Gson();
            Type listType = new TypeToken<List<MsgList>>() {}.getType();
            String msg = sharedPref.getString(SHARED_MESSAGES,"");
            List<MsgList> restoredMessages = gson.fromJson(msg,listType);
            int n = restoredMessages.size();
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
}
