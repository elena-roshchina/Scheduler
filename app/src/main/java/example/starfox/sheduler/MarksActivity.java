package example.starfox.sheduler;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public class MarksActivity extends AppCompatActivity {

    private static final String SHARED_PREF = "MY_SHARED_PREF";
    private static final String SHARED_PREF_LOG = "USER_LOGIN";
    private static final String SHARED_PREF_PASS = "USER_PASS";
    private static final String SHARED_PREF_SESSION = "SESSION";
    private static final String SHARED_SCHEDULE = "SCHEDULE";
    private static final String SHARED_MARKS = "MARKS";
    private static final String SHARED_MESSAGES = "MESSAGES";
    private static final String SHARED_LAST_UPDATE = "LAST_UPDATE";



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_marks);
        setTitle("Зачетка");

        SharedPreferences sharedPref = getSharedPreferences(SHARED_PREF, MODE_PRIVATE);
        RecyclerView recyclerView  = findViewById(R.id.marks_recycle_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);


        if (sharedPref.contains(SHARED_MARKS)){
            Gson gson =  new Gson();
            Type listType = new TypeToken<List<SubjectsList>>() {}.getType();
            String gMarks = sharedPref.getString(SHARED_MARKS,"");
            List<SubjectsList> restoredMarks = gson.fromJson(gMarks,listType);
            int n = restoredMarks.size();
            String s1 = restoredMarks.get(0).getSubject();


            Toast.makeText(this, s1,
                    Toast.LENGTH_SHORT).show();
            // show our data
            MarksAdapter adapter = new MarksAdapter(restoredMarks);
            recyclerView.setAdapter(adapter);

        } else {
            Toast.makeText(this, " Lines not found",
                    Toast.LENGTH_SHORT).show();
        }
    }
}
