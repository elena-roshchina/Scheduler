package example.starfox.sheduler;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DialogSignIn extends DialogFragment {
    private static final String SHARED_PREF = "MY_SHARED_PREF";
    private static final String SHARED_PREF_LOG = "USER_LOGIN";
    private static final String SHARED_PREF_PASS = "USER_PASS";
    private static final String SHARED_PREF_SESSION = "SESSION";

    private SharedPreferences sharedPref;
    private SharedPreferences.Editor editor;
    private String log;
    private String pswd;

    @NonNull
    @Override
    public Dialog onCreateDialog(final Bundle savedInstanceState) {
        // use Builder class for dialog construction
        final Context context = getContext();
        assert context != null;
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = LayoutInflater.from(context);
        View dialogView = inflater.inflate(R.layout.dialog_signin, null);
        // Inflate and set layout for dialog
        // Pass null as the parent view because its going in the dialog box
        sharedPref = context.getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE);
        editor = sharedPref.edit();
        builder.setView(dialogView);

        final EditText enterLogin = dialogView.findViewById(R.id.user_login_dialog);
        final EditText enterPswd = dialogView.findViewById(R.id.user_pswd_dialog);

        // забыли пароль открыть активити с формой отправки емейла
        Button forgetPasswordButton = dialogView.findViewById(R.id.forget_pass_button);
        forgetPasswordButton.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);
        forgetPasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "you need to send your email",
                        Toast.LENGTH_SHORT).show();

                DialogForgetPswd forgetPswd = new DialogForgetPswd();
                if (getFragmentManager() != null) {
                    forgetPswd.show(getFragmentManager(),"Enter email");
                }

            }
        });

        builder.setPositiveButton(R.string.signin, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                log = enterLogin.getText().toString();
                pswd = enterPswd.getText().toString();
                if (log.length() == 2 && pswd.length() == 3) {

                    App.getAuthApi().getData(log, pswd)
                            .enqueue(new Callback<IdentificationModel>() {
                                @Override
                                public void onResponse(Call<IdentificationModel> call,
                                                       @NonNull Response<IdentificationModel> response) {
                                    if (response.body() != null) {
                                        boolean status = response.body().getStatus();
                                        if (status){
                                            editor.putString(SHARED_PREF_LOG,log);
                                            editor.putString(SHARED_PREF_PASS,pswd);

                                            String session = response.body().getSession();

                                            editor.putString(SHARED_PREF_SESSION,session);
                                            editor.apply();
                                            editor.commit();

                                            Toast.makeText(context, "data correct and saved",
                                                    Toast.LENGTH_SHORT).show();
                                        } else {
                                            Toast.makeText(context, "data wrong. Sign in again",
                                                    Toast.LENGTH_SHORT).show();
                                        }
                                    } else {
                                        Toast.makeText(context, "No response",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                }
                                @Override
                                public void onFailure(@NonNull Call<IdentificationModel> call, @NonNull Throwable t) {
                                    Toast.makeText(context,"Sorry, no response",
                                            Toast.LENGTH_SHORT).show();
                                }
                            });
                } else {
                    Toast.makeText(context, "ENTER CORRECT DATA",
                            Toast.LENGTH_SHORT).show();
                }

            }
        })
                .setNegativeButton(R.string.dialog_signin_cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        return builder.create();
        //return super.onCreateDialog(savedInstanceState);
    }
}
