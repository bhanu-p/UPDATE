package com.example.delevere.cbook;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Button;

public class ReportError extends AppCompatActivity {
    EditText errorname,errordetails;
    Button errorbutton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_error);
        Toolbar toolbar = (Toolbar) findViewById(R.id.reporterrortoolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),Home2Activicy.class));
            }
        });
        errorname = (EditText) findViewById(R.id.Error_Name);
        errordetails = (EditText) findViewById(R.id.error_description);
        errorbutton = (Button)findViewById(R.id.error_send_button);






        errorbutton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                boolean cancel = false;
                View focusView = null;
                String name = errorname.getText().toString();
                String details = errordetails.getText().toString();


                if (TextUtils.isEmpty(name)) {
                    errorname.setError(getString(R.string.error_field_required));
                    focusView = errorname;
                    cancel = true;
                }else if(TextUtils.isEmpty(details)){
                    errordetails.setError(getString(R.string.error_field_required));
                    focusView = errordetails;
                    cancel = true;

                }


                if (cancel) {


                    focusView.requestFocus();
                } else {


                    SharedPreferences sharedPreferences = getSharedPreferences(Session.PREFS_NAME,0);
                    String ph = sharedPreferences.getString(Session.KEY_phone,"Phone Number Not Available");
                    String type = "error";
                    UserLoginTask userLoginTask = new UserLoginTask(getApplicationContext());
                    userLoginTask.execute(type,ph,name,details);

                    startActivity(new Intent(getApplicationContext(),Home2Activicy.class));


                }




            }
        });



    }
}
