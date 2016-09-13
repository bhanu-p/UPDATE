package com.example.delevere.cbook;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

/**
 * A login screen that offers login via email/password.
 */
public class ForgotPasswordActivity extends AppCompatActivity  {

    private String mAuthTask = null;

    // UI references.
    private EditText mPhoneView;
    private View mProgressView;
    private View mLoginFormView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        // Set up the login form.
        mPhoneView = (EditText) findViewById(R.id.phone);



        Button mEmailSignInButton = (Button) findViewById(R.id.forgot_email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();

            }
        });

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
    }

    private void attemptLogin() {
        if (mAuthTask != null) {
            return;
        }

        // Reset errors.
        mPhoneView.setError(null);

        // Store values at the time of the login attempt.
        String phone = mPhoneView.getText().toString();

        boolean cancel = false;
        View focusView = null;


        // Check for a valid email address.
        if (!isPhoneValid(phone)) {
            mPhoneView.setError(getString(R.string.error_invalid_phone));
            focusView = mPhoneView;
            cancel = true;
        }
        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        }else {
            SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(Session.PREFS_NAME, 0);

            SharedPreferences.Editor editor = sharedPreferences.edit();

            editor.putString(Session.KEY_phone, phone);

            //Toast.makeText(LoginActivity.this, email, Toast.LENGTH_SHORT).show();
            //Saving values to editor
            editor.commit();

            String type = "forgot";
            UserLoginTask userLoginTask = new UserLoginTask(this);
            //Toast.makeText(LoginActivity.this, email, Toast.LENGTH_SHORT).show();
            userLoginTask.execute(type, phone,phone);

            //startActivity(new Intent(getApplicationContext(),UpdatePasswordActivity.class));
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            //mAuthTask = new UserLoginTask(email, password);
            //mAuthTask.execute((Void) null);
        }
    }

    private boolean isPhoneValid(String phone) {
        //TODO: Replace this with your own logic
        return phone.length() == 10;
    }


}

