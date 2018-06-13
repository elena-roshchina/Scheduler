package example.starfox.sсheduler;


import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


public class DialogForgetPswd extends DialogFragment {

    private static final String ADMIN_EMAIL = "star-fox@yandex.ru";
    private static final String SUBJECT = "запрос логина ЦПСМИ";

    @NonNull
    @Override
    public Dialog onCreateDialog(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final Context context = getContext();
        assert context != null;
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = LayoutInflater.from(context);
        final View dialogView = inflater.inflate(R.layout.forget_password, null);
        boolean sendEmailFlag = false;

        builder.setView(dialogView);

        EditText enterEmail = dialogView.findViewById(R.id.enterEmail);
        enterEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                TextView emailValidation = dialogView.findViewById(R.id.email_validation);

                StringBuilder userEmail = new StringBuilder("");
                userEmail.append(s);
                if (emailCheck(userEmail.toString())){
                    emailValidation.setTextColor(Color.GREEN);
                    emailValidation.setText("EMAIL CORRECT");
                } else {
                    emailValidation.setTextColor(Color.RED);
                    emailValidation.setText("EMAIL INVALID");
                }
            }

            @Override
            public void afterTextChanged(Editable s) { }
        });

        builder.setPositiveButton(R.string.contact_admin, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // send email to admin
                EditText enterEmail = dialogView.findViewById(R.id.enterEmail);
                String userEmail = enterEmail.getText().toString();
                if (emailCheck(userEmail)){
                    final Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
                    emailIntent.setType("plain/text");
                    emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[] { ADMIN_EMAIL });
                    emailIntent.putExtra(Intent.EXTRA_SUBJECT, SUBJECT);
                    emailIntent.putExtra(Intent.EXTRA_TEXT, userEmail);
                    startActivity(Intent.createChooser(emailIntent,
                            "Отправка письма..."));
                    dismiss();
                } else {
                    Toast.makeText(context, "email is not valid " + userEmail,
                            Toast.LENGTH_SHORT).show();
                    dismiss();
                }
            }
        })
                .setNegativeButton(R.string.dialog_signin_cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dismiss();
                    }
                });

        return builder.create();
    } // end of onCreateDialog

    private boolean emailCheck(String s){
        return s.matches("(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+" +
                        "(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"" +
                        "(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@" +
                        "(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9]" +
                        "(?:[a-z0-9-]*[a-z0-9])?|\\[" +
                        "(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}" +
                        "(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:" +
                        "(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])");
        }
}
