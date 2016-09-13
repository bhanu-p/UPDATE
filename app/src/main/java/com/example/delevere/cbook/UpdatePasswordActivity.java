package com.example.delevere.cbook;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * A login screen that offers login via email/password.
 */
public class UpdatePasswordActivity extends AppCompatActivity{

    /**
     * Id to identity READ_CONTACTS permission request.
     */
    private static final int REQUEST_READ_CONTACTS = 0;

    private UserLoginTask mAuthTask = null;

    // UI references.

    private EditText mNewPasswordView,mConformPasswordView;
    private View mProgressView;
    private View mLoginFormView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_password);
        // Set up the login form.
        mNewPasswordView = (EditText) findViewById(R.id.email);

        mConformPasswordView = (EditText) findViewById(R.id.password);
        mConformPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
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
        mNewPasswordView.setError(null);
        mConformPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String Newpass = mNewPasswordView.getText().toString();
        String Conformpass = mConformPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.


        // Check for a valid email address.
        if (TextUtils.isEmpty(Newpass)) {
            mNewPasswordView.setError(getString(R.string.error_field_required));
            focusView = mNewPasswordView;
            cancel = true;
        }
        else if (!isPasswordValid(Newpass)) {
            mNewPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mNewPasswordView;
            cancel = true;
        }
        else if ( TextUtils.isEmpty(Conformpass)) {
            mConformPasswordView.setError(getString(R.string.error_field_required));
            focusView = mConformPasswordView;
            cancel = true;
        }
        else if ( !isPasswordValid(Conformpass)) {
            mConformPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mConformPasswordView;
            cancel = true;
        }else if (!Newpass.equals(Conformpass)) {
            mNewPasswordView.setError(getString(R.string.error_password_mismatch));
            mConformPasswordView.setError(getString(R.string.error_password_mismatch));
            focusView = mNewPasswordView;
            focusView = mConformPasswordView;
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

            String type = "updatepassword";
            UserLoginTask userLoginTask = new UserLoginTask(this);
            userLoginTask.execute(type,phone,Newpass);


        }
    }



    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
    }

    /**
     * Shows the progress UI and hides the login form.
     */
}

