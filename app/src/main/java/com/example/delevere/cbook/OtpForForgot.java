package com.example.delevere.cbook;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Button;
import android.widget.Toast;

public class OtpForForgot extends AppCompatActivity {
    private TextView motp,resend;
    private Button submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp_for_forgot);
        motp = (EditText) findViewById(R.id.otpforgot);
        resend = (TextView) findViewById(R.id.resend_forgot_otp);

        submit = (Button) findViewById(R.id.otp_forgot_sign_in_button);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validate();
                //startActivity(new Intent(getApplicationContext(),RegisterActivity.class));
            }
        });

        resend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPreferences = getSharedPreferences(Session.PREFS_NAME, 0);
                String phone = sharedPreferences.getString(Session.KEY_phone, "9999999999");

                String type = "forgot";
                UserLoginTask userLoginTask = new UserLoginTask(OtpForForgot.this);
                //Toast.makeText(LoginActivity.this, email, Toast.LENGTH_SHORT).show();
                userLoginTask.execute(type, phone, phone);


                //validate();
                //startActivity(new Intent(getApplicationContext(),RegisterActivity.class));
            }
        });

    }
    private void validate() {
        motp.setError(null);
        String otp = motp.getText().toString();

        boolean cancel = false;
        View focusView = null;

        if (!isOtpValid(otp)) {
            motp.setError(getString(R.string.error_invalid_otp));
            focusView = motp;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            SharedPreferences sharedPreferences = getSharedPreferences(Session.PREFS_NAME,0);
            String phone = sharedPreferences.getString(Session.KEY_phone,"9999999999");
            //Showing the current logged in email to textview

            String type = "otpforgot";
            UserLoginTask userLoginTask = new UserLoginTask(this);
            userLoginTask.execute(type,otp,phone);
            //Toast.makeText(OtpActivity.this, "ok", Toast.LENGTH_SHORT).show();
        }

    }

    private boolean isOtpValid(String phone) {
        //TODO: Replace this with your own logic
        return phone.length() == 6;
    }
}
