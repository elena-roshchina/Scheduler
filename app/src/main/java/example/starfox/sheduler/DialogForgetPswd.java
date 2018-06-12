package example.starfox.sheduler;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class DialogForgetPswd extends DialogFragment {

    private static final String SHARED_PREF = "MY_SHARED_PREF";
    private static final String SHARED_PREF_ADMIN_EMAIL = "ADMIN_EMAIL";
    private SharedPreferences sharedPref;


    @NonNull
    @Override
    public Dialog onCreateDialog(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final Context context = getContext();
        assert context != null;
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = LayoutInflater.from(context);
        View dialogView = inflater.inflate(R.layout.forget_password, null);

        builder.setView(dialogView);
        sharedPref = context.getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE);

        EditText enterEmail = dialogView.findViewById(R.id.enterEmail);

        final String adminEmail = sharedPref.getString(SHARED_PREF_ADMIN_EMAIL,"");

        builder.setPositiveButton(R.string.signin, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // send email to admin
                Toast.makeText(context, "email sent",
                        Toast.LENGTH_SHORT).show();

            }
        })
                .setNegativeButton(R.string.dialog_signin_cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        return builder.create();




    }
}
