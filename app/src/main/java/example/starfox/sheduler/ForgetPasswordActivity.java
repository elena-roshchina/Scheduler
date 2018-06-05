package example.starfox.sheduler;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ForgetPasswordActivity extends AppCompatActivity {

    private static final String SHARED_PREF = "MY_SHARED_PREF";
    private static final String SHARED_PREF_SESSION = "SESSION";
    private SharedPreferences sharedPref;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forget_password);
        EditText enterEmail = findViewById(R.id.enterEmail);

        sharedPref = getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE);
        final String session = sharedPref.getString(SHARED_PREF_SESSION,"");

        Button sendEmailButton = findViewById(R.id.sendEmailButton);

        sendEmailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // послать этот емейл емейлом администратору  admin@starfox.spb.ru
                // создать тост

            }
        });




    }
}
